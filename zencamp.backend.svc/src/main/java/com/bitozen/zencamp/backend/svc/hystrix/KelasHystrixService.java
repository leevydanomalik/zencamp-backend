package com.bitozen.zencamp.backend.svc.hystrix;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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

import com.bitozen.zencamp.backend.domain.Kelas;
import com.bitozen.zencamp.backend.domain.repository.KelasRepository;
import com.bitozen.zencamp.backend.dto.KelasDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.assembler.KelasDTOAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;

import lombok.extern.slf4j.Slf4j;

@Service
@PropertySource("classpath:error-message.properties")
@Slf4j
public class KelasHystrixService {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private KelasDTOAssembler assembler;
	
	@Autowired
	private KelasRepository repository;
	
	@Value("${data.integrity.exception.message}")
	private String INTEGRITY_ERROR_MSG;

	@Value("${data.not.found.message}")
	private String DATA_NOT_FOUND_MSG;
	
	@HystrixCommand(fallbackMethod = "defaultGetKelasAllFallback")
    @Cacheable(value="getKelasAllCache", key = "#p0")
	public GenericResponseDTO<List<KelasDTO>> getKelasAll(GetListRequestDTO dto) {
		try {
			 Pageable page = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("kelasCreational.createdDate").descending());
			 Page<Kelas> datas = repository.findAll(page);
			 if (datas == null) {
	                log.info(objectMapper.writeValueAsString(
	                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Kelas", new Date(), "Rest", "204", "DATA NOT FOUND")));
	                 return new GenericResponseDTO().noDataFoundResponse();
	            }
	            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
	                    ProjectType.CRUD, "Kelas", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
	                    new GenericResponseDTO().successResponse().getMessage())));
	            return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
		} catch (Exception e) {
   		log.info(e.getMessage());
   		try {
               log.info(objectMapper.writeValueAsString(
                       LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Kelas", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
           } catch (JsonProcessingException ex) {
               log.info(ex.getMessage());
           }
           return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getLocalizedMessage());
		}
	}
	
	@HystrixCommand(fallbackMethod = "defaultPostKelasFallback")
    @Caching(
        evict = {
            @CacheEvict(value = "getKelasAllCache", allEntries= true)
        })
public GenericResponseDTO<KelasDTO> postKelas(KelasDTO dto) {
	try {
		Optional<Kelas> data = repository.findOneByKelasID(dto.getKelasID());
		if (data.isPresent()) {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Kelas", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getKelasID()))));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getKelasID()));
        }
        repository.save(assembler.toDomain(dto));
        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Kelas", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                new GenericResponseDTO().successResponse().getMessage())));
        return new GenericResponseDTO().successResponse(dto);
	} catch (Exception e) {
        log.info(e.getMessage());
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Kelas", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
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
	
	public GenericResponseDTO<List<KelasDTO>> defaultGetKelasAllFallback(GetListRequestDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<List<KelasDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	}
	
	public GenericResponseDTO<KelasDTO> defaultPostKelasFallback(KelasDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<KelasDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	}
}
