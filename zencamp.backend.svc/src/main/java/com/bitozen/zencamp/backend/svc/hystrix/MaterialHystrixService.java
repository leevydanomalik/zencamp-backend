/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.hystrix;

import com.bitozen.zencamp.backend.domain.Material;
import com.bitozen.zencamp.backend.domain.repository.MaterialRepository;
import com.bitozen.zencamp.backend.dto.MaterialDTO;
import com.bitozen.zencamp.backend.dto.MaterialUpdateDTO;
import com.bitozen.zencamp.backend.dto.common.DeleteRequestDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.assembler.MaterialDTOAssembler;
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
 * @author Dumayangsari
 */
@Service
@PropertySource("classpath:error-message.properties")
@Slf4j
public class MaterialHystrixService {
        @Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MaterialDTOAssembler assembler;
	
	@Autowired
	private MaterialRepository repository;
	
	@Value("${data.integrity.exception.message}")
	private String INTEGRITY_ERROR_MSG;

	@Value("${data.not.found.message}")
	private String DATA_NOT_FOUND_MSG;
        
        @HystrixCommand(fallbackMethod = "defaultGetMaterialAllFallback")
        @Cacheable(value="getMaterialAllCache", key = "#p0")
	public GenericResponseDTO<List<MaterialDTO>> getMaterialAll(GetListRequestDTO dto) {
            try {
		Pageable page = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("materialCreational.createdDate").descending());
        	Page<Material> datas = repository.findAll(page);
		if (datas == null) {
	            log.info(objectMapper.writeValueAsString(
	            LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", "204", "DATA NOT FOUND")));
	            return new GenericResponseDTO().noDataFoundResponse();
	        }
	        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Material", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
	            new GenericResponseDTO().successResponse().getMessage())));
	        return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
            }catch (Exception e) {
   		log.info(e.getMessage());
   		try {
                    log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
                } catch (JsonProcessingException ex) {
                    log.info(ex.getMessage());
                }
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getLocalizedMessage());
            }
	}
	
	@HystrixCommand(fallbackMethod = "defaultGetMaterialByIDFallback")
        @Cacheable(value="getMaterialByIDCache", key = "#p0")
        public GenericResponseDTO<MaterialDTO> getMaterialByID(String materialID) {
            try {
		Optional<Material> data = repository.findOneByMaterialID(materialID);
		if (data == null) {
                    log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", "204", "DATA NOT FOUND")));
                    return new GenericResponseDTO().noDataFoundResponse();
                }
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Material", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().successResponse(assembler.toDTO(data.get()));
            } catch (Exception e) {
		log.info(e.getMessage());
		try {
                    log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", 
                                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
                } catch (JsonProcessingException ex) {
                    log.info(ex.getMessage());
                }
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getLocalizedMessage());
            }
        }
        
        @HystrixCommand(fallbackMethod = "defaultGetMaterialByNameFallback")
        @Cacheable(value="getMaterialByNameCache", key = "#p0")
        public GenericResponseDTO<List<MaterialDTO>> getMaterialByName(GetListRequestDTO dto){
            try {
		Pageable pageable = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("materialCreational.createdDate").descending());
                String materialName = String.valueOf(dto.getParams().get("materialName"));
                Page<Material> datas = repository.findAllByMaterialName(materialName, pageable);
                if (datas == null) {
                    log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", "204", "DATA NOT FOUND")));
                    return new GenericResponseDTO().noDataFoundResponse();
                }
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Material", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
            } catch (Exception e) {
		log.info(e.getMessage());
		try {
                    log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", 
                                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
                } catch (JsonProcessingException ex) {
                    log.info(ex.getMessage());
                }
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getLocalizedMessage());
            }
        }
	
	@HystrixCommand(fallbackMethod = "defaultPostMaterialFallback")
        @Caching(
        evict = {
            @CacheEvict(value = "getMaterialByIDCache", key="#dto.materialID"), //data di cache terupdate
            @CacheEvict(value = "getMaterialAllCache", allEntries= true) 
        })
        public GenericResponseDTO<MaterialDTO> postMaterial(MaterialDTO dto) {
	try {
            Optional<Material> data = repository.findOneByMaterialID(dto.getMaterialID());
            if (data.isPresent()) {
                log.info(objectMapper.writeValueAsString(
                LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", 
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getMaterialID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getMaterialID()));
            }
            repository.save(assembler.toDomain(dto));
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Material", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(dto);
	} catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", 
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

    @HystrixCommand(fallbackMethod = "defaultPutMaterialFallback")
    @Caching(
    evict = {
      		@CacheEvict(value = "getMaterialByIDCache", key="#dto.materialID"),
                @CacheEvict(value = "getMaterialkAllCache", allEntries= true)
            
    })
    public GenericResponseDTO<MaterialDTO> putMaterial(MaterialUpdateDTO dto) {
	try {
            Optional<Material> data = repository.findOneByMaterialID(dto.getMaterialID());
            if (!data.isPresent()) {
    		log.info(objectMapper.writeValueAsString(
                LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", 
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, 
                        dto.getMaterialID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getMaterialID()));
            }         
            data.get().setMaterialName(dto.getMaterialName());
            data.get().setMaterialKIMAP(dto.getMaterialKIMAP());
            data.get().setMaterialDesc(dto.getMaterialDesc());
            data.get().setMaterialType(dto.getMaterialType());
            data.get().setMaterialUoM(dto.getMaterialUoM());
            data.get().setMaterialPrice(dto.getMaterialPrice());
            data.get().setMaterialQty(dto.getMaterialQty());
            data.get().getMaterialCreational().setModifiedBy(dto.getRequestBy() == null ? "SYSTEM" 
                : dto.getRequestBy() == null? "SYSTEM": dto.getRequestBy());
            data.get().getMaterialCreational().setModifiedDate(dto.getRequestDate() == null? (new Date())
                .toInstant().atZone(ZoneId.systemDefault()): dto.getRequestDate().toInstant().atZone(ZoneId.systemDefault()));
	 
            repository.save(data.get());
    		
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Material", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
	} catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", 
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
	
    @HystrixCommand(fallbackMethod = "defaultDeleteMaterialFallback")
    @Caching(
    evict = {
        	@CacheEvict(value = "getMaterialByIDCache", key="#dto.referenceID"),
                @CacheEvict(value = "getMaterialAllCache", allEntries= true)
    })
    public GenericResponseDTO<MaterialDTO> deleteMaterial(DeleteRequestDTO dto) {
	try {
            Optional<Material> data = repository.findOneByMaterialID(dto.getReferenceID());
            if (!data.isPresent()) {
                log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                        MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()));
            }
            repository.delete(data.get());
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Material", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
	} catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Material", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                e.getLocalizedMessage());
	}
    }
		
    public GenericResponseDTO<List<MaterialDTO>> defaultGetMaterialAllFallback(GetListRequestDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<List<MaterialDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data"  
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<MaterialDTO> defaultGetMaterialByIDFallback(String materialID, Throwable e) throws IOException {
	return new GenericResponseDTO<MaterialDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
    
    public GenericResponseDTO<List<MaterialDTO>> defaultGetMaterialByNameFallback(GetListRequestDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<List<MaterialDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<MaterialDTO> defaultPostMaterialFallback(MaterialDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<MaterialDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<MaterialDTO> defaultPutMaterialFallback(MaterialUpdateDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<MaterialDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
            : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<MaterialDTO> defaultDeleteMaterialFallback(DeleteRequestDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<MaterialDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
            : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
}
