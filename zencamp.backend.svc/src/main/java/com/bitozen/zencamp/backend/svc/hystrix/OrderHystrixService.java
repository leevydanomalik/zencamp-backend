/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.hystrix;

import com.bitozen.zencamp.backend.domain.OrderCustomer;
import com.bitozen.zencamp.backend.domain.repository.OrderRepository;
import com.bitozen.zencamp.backend.dto.OrderDTO;
import com.bitozen.zencamp.backend.dto.OrderUpdateDTO;
import com.bitozen.zencamp.backend.dto.common.DeleteRequestDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.assembler.OrderDTOAssembler;
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
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

/**
 *
 * @author Dumayangsari
 */
@Service
@PropertySource("classpath:error-message.properties")
@Slf4j
public class OrderHystrixService {
    @Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private OrderDTOAssembler assembler;
	
	@Autowired
	private OrderRepository repository;
	
	@Value("${data.integrity.exception.message}")
	private String INTEGRITY_ERROR_MSG;

	@Value("${data.not.found.message}")
	private String DATA_NOT_FOUND_MSG;
	
	@HystrixCommand(fallbackMethod = "defaultGetOrderCustomerAllFallback")
        @Cacheable(value="getOrderCustomerAllCache", key = "#p0")
	public GenericResponseDTO<List<OrderDTO>> getOrderCustomerAll(GetListRequestDTO dto) {
            try {
		Pageable page = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("customerCreational.createdDate").descending());
        	Page<OrderCustomer> datas = repository.findAll(page);
		if (datas == null) {
	            log.info(objectMapper.writeValueAsString(
	            LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", "204", "DATA NOT FOUND")));
	            return new GenericResponseDTO().noDataFoundResponse();
	        }
	        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
	            new GenericResponseDTO().successResponse().getMessage())));
	        return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
            }catch (Exception e) {
   		log.info(e.getMessage());
   		try {
                    log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
                } catch (JsonProcessingException ex) {
                    log.info(ex.getMessage());
                }
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getLocalizedMessage());
            }
	}
	
	@HystrixCommand(fallbackMethod = "defaultGetOrderCustomerByIDFallback")
        @Cacheable(value="getOrderCustomerByIDCache", key = "#p0")
        public GenericResponseDTO<OrderDTO> getOrderCustomerByID(String customerID) {
            try {
		Optional<OrderCustomer> data = repository.findOneByCustomerID(customerID);
		if (data == null) {
                    log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", "204", "DATA NOT FOUND")));
                    return new GenericResponseDTO().noDataFoundResponse();
                }
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().successResponse(assembler.toDTO(data.get()));
            } catch (Exception e) {
		log.info(e.getMessage());
		try {
                    log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", 
                                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
                } catch (JsonProcessingException ex) {
                    log.info(ex.getMessage());
                }
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getLocalizedMessage());
            }
        }
        
        /*@HystrixCommand(fallbackMethod = "defaultGetCustomerByNameFallback")
        @Cacheable(value="getCustomerByNameCache", key = "#p0")
        public GenericResponseDTO<List<CustomerDTO>> getCustomerByName(GetListRequestDTO dto){
            try {
		Pageable pageable = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("customerCreational.createdDate").descending());
                String customerName = String.valueOf(dto.getParams().get("customerName"));
                Page<Customer> datas = repository.findAllByCustomerName(customerName, pageable);
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
        }*/
	
	@HystrixCommand(fallbackMethod = "defaultPostOrderCustomerFallback")
        @Caching(
        evict = {
            @CacheEvict(value = "getOrderCustomerByIDCache", key="#dto.customerID"), //data di cache terupdate
            @CacheEvict(value = "getOrderCustomerAllCache", allEntries= true) 
        })
        public GenericResponseDTO<OrderDTO> postOrderCustomer(OrderDTO dto) {
	try {
            Optional<OrderCustomer> data = repository.findOneByCustomerID(dto.getCustomerID());
            if (data.isPresent()) {
                log.info(objectMapper.writeValueAsString(
                LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", 
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getCustomerID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getCustomerID()));
            }
            repository.save(assembler.toDomain(dto));
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(dto);
	} catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            if (e instanceof TransactionSystemException) {
                if (((TransactionSystemException) e).getRootCause() instanceof ConstraintViolationException) {
                    Set<ConstraintViolation<?>> data = ((ConstraintViolationException)(
                        (TransactionSystemException) e).getRootCause()).getConstraintViolations();
                    String msg = new ArrayList<>(data).get(0).getMessage();
                    return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        msg);
                }
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                e.getLocalizedMessage());
        }
    }

    @HystrixCommand(fallbackMethod = "defaultPutOrderCustomerFallback")
    @Caching(
    evict = {
      		@CacheEvict(value = "getOrderCustomerByIDCache", key="#dto.customerID"),
                @CacheEvict(value = "getOrderCustomerkAllCache", allEntries= true)
            
    })
    public GenericResponseDTO<OrderDTO> putOrderCustomer(OrderUpdateDTO dto) {
	try {
            Optional<OrderCustomer> data = repository.findOneByCustomerID(dto.getCustomerID());
            if (!data.isPresent()) {
    		log.info(objectMapper.writeValueAsString(
                LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", 
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, 
                        dto.getCustomerID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getCustomerID()));
            }         
            data.get().setCustomerName(dto.getCustomerName());
            data.get().setNoTelp(dto.getNoTelp());
            data.get().setAlamatAsal(dto.getAlamatAsal());
            data.get().setAlamatTujuan(dto.getAlamatTujuan());
            data.get().setMaterialKIMAP(dto.getMaterialKIMAP());
            data.get().setMaterialName(dto.getMaterialName());
            data.get().setMaterialDesc(dto.getMaterialDesc());
            data.get().setMaterialType(dto.getMaterialType());
            data.get().setMaterialUoM(dto.getMaterialUoM());
            data.get().setMaterialPrice(dto.getMaterialPrice());
            data.get().setOrderQty(dto.getOrderQty());
            data.get().setOrderDate(dto.getOrderDate());
            data.get().getCustomerCreational().setModifiedBy(dto.getRequestBy() == null ? "SYSTEM" 
                : dto.getRequestBy() == null? "SYSTEM": dto.getRequestBy());
            data.get().getCustomerCreational().setModifiedDate(dto.getRequestDate() == null? (new Date())
                .toInstant().atZone(ZoneId.systemDefault()): dto.getRequestDate().toInstant().atZone(ZoneId.systemDefault()));
	 
            repository.save(data.get());
    		
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
	} catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", 
                                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            if (e instanceof TransactionSystemException) {
                if (((TransactionSystemException) e).getRootCause() instanceof ConstraintViolationException) {
                    Set<ConstraintViolation<?>> data = ((ConstraintViolationException) ((TransactionSystemException) e)
                        .getRootCause()).getConstraintViolations();
                    String msg = new ArrayList<>(data).get(0).getMessage();
                    return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        , msg);
                }
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e.getLocalizedMessage());
	}
    }
	
    @HystrixCommand(fallbackMethod = "defaultDeleteOrderCustomerFallback")
    @Caching(
    evict = {
        	@CacheEvict(value = "getOrderCustomerByIDCache", key="#dto.referenceID"),
                @CacheEvict(value = "getOrderCustomerAllCache", allEntries= true)
    })
    public GenericResponseDTO<OrderDTO> deleteOrderCustomer(DeleteRequestDTO dto) {
	try {
            Optional<OrderCustomer> data = repository.findOneByCustomerID(dto.getReferenceID());
            if (!data.isPresent()) {
                log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                        MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()));
            }
            repository.delete(data.get());
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
	} catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "OrderCustomer", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                e.getLocalizedMessage());
	}
    }
		
    public GenericResponseDTO<List<OrderDTO>> defaultGetOrderCustomerAllFallback(GetListRequestDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<List<OrderDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data"  
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<OrderDTO> defaultGetOrderCustomerByIDFallback(String customerID, Throwable e) throws IOException {
	return new GenericResponseDTO<OrderDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
    
    /*public GenericResponseDTO<List<CustomerDTO>> defaultGetCustomerByNameFallback(GetListRequestDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<List<CustomerDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }*/
	
    public GenericResponseDTO<OrderDTO> defaultPostOrderCustomerFallback(OrderDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<OrderDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<OrderDTO> defaultPutOrderCustomerFallback(OrderUpdateDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<OrderDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
            : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<OrderDTO> defaultDeleteOrderCustomerFallback(DeleteRequestDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<OrderDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
            : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
}
