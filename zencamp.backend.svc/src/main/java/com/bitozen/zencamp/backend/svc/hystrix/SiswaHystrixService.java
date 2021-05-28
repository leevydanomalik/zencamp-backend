package com.bitozen.zencamp.backend.svc.hystrix;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import com.bitozen.zencamp.backend.domain.Kelas;
import com.bitozen.zencamp.backend.domain.Siswa;
import com.bitozen.zencamp.backend.domain.repository.KelasRepository;
import com.bitozen.zencamp.backend.domain.repository.SiswaRepository;
import com.bitozen.zencamp.backend.dto.SiswaCreateDTO;
import com.bitozen.zencamp.backend.dto.SiswaDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.assembler.SiswaDTOAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;

import lombok.extern.slf4j.Slf4j;

@Service
@PropertySource("classpath:error-message.properties")
@Slf4j
public class SiswaHystrixService {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SiswaDTOAssembler assembler;
	
	@Autowired
	private SiswaRepository repository;
	
	@Autowired
	private KelasRepository kelasRepository;
	
	@Value("${data.integrity.exception.message}")
	private String INTEGRITY_ERROR_MSG;

	@Value("${data.not.found.message}")
	private String DATA_NOT_FOUND_MSG;
	
	@HystrixCommand(fallbackMethod = "defaultPostSiswaFallback")
    @Caching(
        evict = {
            @CacheEvict(value = "getKelasAllCache", allEntries= true)
        })
	public GenericResponseDTO<SiswaCreateDTO> postSiswa(SiswaCreateDTO dto) {
	try {
		Optional<Siswa> data = repository.findOneBySiswaID(dto.getSiswa().getSiswaID());
		if (data.isPresent()) {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Siswa", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getSiswa().getSiswaID()))));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getSiswa().getSiswaID()));
        }
		
		Optional<Kelas> kelas = kelasRepository.findOneByKelasID(dto.getKelasID());
		
		kelas.get().getListSiswa().add(assembler.toDomain(dto.getSiswa()));
		kelasRepository.save(kelas.get());
        
        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Siswa", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                new GenericResponseDTO().successResponse().getMessage())));
        return new GenericResponseDTO().successResponse(dto);
	} catch (Exception e) {
        log.info(e.getMessage());
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Siswa", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
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
	
	public GenericResponseDTO<SiswaCreateDTO> defaultPostSiswaFallback(SiswaCreateDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<SiswaCreateDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	}
}
