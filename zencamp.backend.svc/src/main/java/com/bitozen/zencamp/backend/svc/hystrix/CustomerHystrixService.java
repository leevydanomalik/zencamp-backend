/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.hystrix;

import com.bitozen.zencamp.backend.domain.Customer;
import com.bitozen.zencamp.backend.domain.repository.CustomerRepository;
import com.bitozen.zencamp.backend.dto.CustomerDTO;
import com.bitozen.zencamp.backend.dto.CustomerUpdateDTO;
import com.bitozen.zencamp.backend.dto.common.DeleteRequestDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.assembler.CustomerDTOAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

/**
 *
 * @author Jeremia
 */
@Service
@PropertySource("classpath:error-message.properties")
@Slf4j
public class CustomerHystrixService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerDTOAssembler assembler;

    @Autowired
    private CustomerRepository repository;

    @Value("${data.integrity.exception.message}")
    private String INTEGRITY_ERROR_MSG;

    @Value("${data.not.found.message}")
    private String DATA_NOT_FOUND_MSG;

    @HystrixCommand(fallbackMethod = "defaultGetCustomerAllFallback")
    @Cacheable(value = "getCustomerAllCache", key = "#p0")
    public GenericResponseDTO<List<CustomerDTO>> getCustomerAll(GetListRequestDTO dto) {
        try {
            Pageable page = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("Customercreational.createdDate").descending());
            Page<Customer> datas = repository.findAll(page);
            if (datas == null) {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", "204", "DATA NOT FOUND")));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Customer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
        } catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getLocalizedMessage());
        }
    }

    @HystrixCommand(fallbackMethod = "defaultPostCustomerFallback")
    @Caching(
            evict = {
                @CacheEvict(value = "getCustomerByfullnameCache", key = "#dto.fullname")
                ,
                @CacheEvict(value = "getCustomerAllCache", allEntries = true)
                ,
                @CacheEvict(value = "getCustomerByNameCache", allEntries = true)
                })
    public GenericResponseDTO<CustomerDTO> postCustomer(CustomerDTO dto) {
        try {
//            Optional<Customer> data = repository.findOneByFullname(dto.getFullname());
//            if (data.isPresent()) {
//                log.info(objectMapper.writeValueAsString(
//                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getFullname()))));
//                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getFullname()));
//            }
            repository.save(assembler.toDomain(dto));
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Customer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(dto);
        } catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            if (e instanceof TransactionSystemException) {
                if (((TransactionSystemException) e).getRootCause() instanceof ConstraintViolationException) {
                    Set<ConstraintViolation<?>> data = ((ConstraintViolationException) ((TransactionSystemException) e).getRootCause()).getConstraintViolations();
                    String msg = new ArrayList<>(data).get(0).getMessage();
                    return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), msg);
                }
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }
    
    @HystrixCommand(fallbackMethod = "defaultGetCustomerByIDFallback")
    @Cacheable(value="getCustomerByIDCache", key = "#p0")
    public GenericResponseDTO<CustomerDTO> getCustomerByID(String customerid) {
	try {
		Optional<Customer> data = repository.findOneByCustomerid(customerid);
		if (data == null) {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", "204", "DATA NOT FOUND")));
             return new GenericResponseDTO().noDataFoundResponse();
        }
        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Customer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                new GenericResponseDTO().successResponse().getMessage())));
        return new GenericResponseDTO().successResponse(assembler.toDTO(data.get()));
	} catch (Exception e) {
		log.info(e.getMessage());
		try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getLocalizedMessage());
	}
    }
    
    @HystrixCommand(fallbackMethod = "defaultGetCustomerByNameFallback")
        @Cacheable(value="getCustomerByNameCache", key = "#p0")
        public GenericResponseDTO<List<CustomerDTO>> getCustomerByFullname(GetListRequestDTO dto){
            try {
		Pageable pageable = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("Customercreational.createdDate").descending());
                String fullname = String.valueOf(dto.getParams().get("fullname"));
                Page<Customer> datas = repository.findAllByFullname(pageable, fullname);
                if (datas == null) {
                    log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", "204", "DATA NOT FOUND")));
                    return new GenericResponseDTO().noDataFoundResponse();
                }
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Customer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
            } catch (Exception e) {
		log.info(e.getMessage());
		try {
                    log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", 
                                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
                } catch (JsonProcessingException ex) {
                    log.info(ex.getMessage());
                }
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getLocalizedMessage());
            }
        }

    @HystrixCommand(fallbackMethod = "defaultPutCustomerFallback")
    @Caching(
            evict = {
                @CacheEvict(value = "getCustomerByfullnameCache", key = "#dto.fullname")
                ,
                @CacheEvict(value = "getCustomerAllCache", allEntries = true)

            })
    public GenericResponseDTO<CustomerDTO> putCustomer(CustomerUpdateDTO dto) {
        try {
            Optional<Customer> data = repository.findOneByFullname(dto.getFullname());
            if (!data.isPresent()) {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getFullname()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getFullname()));
            }

            data.get().setCustomerid(dto.getCustomerid());
            data.get().setPhonenumber(dto.getPhonenumber());
            data.get().setFullname(dto.getFullname());
            data.get().setOrderto(dto.getOrderto());
            data.get().setOrderfrom(dto.getOrderfrom());
            data.get().getCustomercreational().setModifiedBy(dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy());
            data.get().getCustomercreational().setModifiedDate(dto.getRequestDate() == null ? (new Date()).toInstant().atZone(ZoneId.systemDefault()) : dto.getRequestDate().toInstant().atZone(ZoneId.systemDefault()));

            repository.save(data.get());

            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Customer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
        } catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            if (e instanceof TransactionSystemException) {
                if (((TransactionSystemException) e).getRootCause() instanceof ConstraintViolationException) {
                    Set<ConstraintViolation<?>> data = ((ConstraintViolationException) ((TransactionSystemException) e).getRootCause()).getConstraintViolations();
                    String msg = new ArrayList<>(data).get(0).getMessage();
                    return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), msg);
                }
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }
    }

    @HystrixCommand(fallbackMethod = "defaultDeleteCustomerFallback")
    @Caching(
            evict = {
                @CacheEvict(value = "getCustomerByIDCache", key = "#dto.referenceID")
                ,
                @CacheEvict(value = "getCustomerAllCache", allEntries = true)

            })
    public GenericResponseDTO<CustomerDTO> deleteCustomer(DeleteRequestDTO dto) {
        try {
            Optional<Customer> data = repository.findOneByFullname(dto.getReferenceID());
            if (!data.isPresent()) {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()));
            }
            repository.delete(data.get());

            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Customer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
        } catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Customer", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getLocalizedMessage());
        }
    }
    public GenericResponseDTO<List<CustomerDTO>> defaultGetCustomerByNameFallback(GetListRequestDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<List<CustomerDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    public GenericResponseDTO<List<CustomerDTO>> defaultGetCustomerAllFallback(GetListRequestDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<List<CustomerDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }

    public GenericResponseDTO<CustomerDTO> defaultPostCustomerFallback(CustomerDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<CustomerDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
    public GenericResponseDTO<CustomerDTO> defaultGetCustomerByIDFallback(String customerid, Throwable e) throws IOException {
		 return new GenericResponseDTO<CustomerDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	}
    
    public GenericResponseDTO<CustomerDTO> defaultPutCustomerFallback(CustomerUpdateDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<CustomerDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	}
    
    public GenericResponseDTO<CustomerDTO> defaultDeleteCustomerFallback(DeleteRequestDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<CustomerDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	 }

}
