package com.bitozen.zencamp.backend.svc.hystrix;

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

import com.bitozen.zencamp.backend.domain.Game;
import com.bitozen.zencamp.backend.domain.repository.GameRepository;
import com.bitozen.zencamp.backend.dto.GameDTO;
import com.bitozen.zencamp.backend.dto.GameUpdateDTO;
import com.bitozen.zencamp.backend.dto.common.DeleteRequestDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.assembler.GameDTOAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;

import lombok.extern.slf4j.Slf4j;

@Service
@PropertySource("classpath:error-message.properties")
@Slf4j
public class GameHystrixService {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private GameDTOAssembler assembler;
	
	@Autowired
	private GameRepository repository;
	
	@Value("${data.integrity.exception.message}")
	private String INTEGRITY_ERROR_MSG;

	@Value("${data.not.found.message}")
	private String DATA_NOT_FOUND_MSG;
	
	@HystrixCommand(fallbackMethod = "defaultGetGameAllFallback")
        @Cacheable(value="getGameAllCache", key = "#p0")
	public GenericResponseDTO<List<GameDTO>> getGameAll(GetListRequestDTO dto) {
            try {
		Pageable page = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("gameCreational.createdDate").descending());
        	Page<Game> datas = repository.findAll(page);
		if (datas == null) {
	            log.info(objectMapper.writeValueAsString(
	            LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", "204", "DATA NOT FOUND")));
	            return new GenericResponseDTO().noDataFoundResponse();
	        }
	        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Game", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
	            new GenericResponseDTO().successResponse().getMessage())));
	        return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
            }catch (Exception e) {
   		log.info(e.getMessage());
   		try {
                    log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
                } catch (JsonProcessingException ex) {
                    log.info(ex.getMessage());
                }
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getLocalizedMessage());
            }
	}
	
	@HystrixCommand(fallbackMethod = "defaultGetGameByIDFallback")
        @Cacheable(value="getGameByIDCache", key = "#p0")
        public GenericResponseDTO<GameDTO> getGameByID(String gameID) {
            try {
		Optional<Game> data = repository.findOneByGameID(gameID);
		if (data == null) {
                    log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", "204", "DATA NOT FOUND")));
                    return new GenericResponseDTO().noDataFoundResponse();
                }
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Game", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().successResponse(assembler.toDTO(data.get()));
            } catch (Exception e) {
		log.info(e.getMessage());
		try {
                    log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", 
                                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
                } catch (JsonProcessingException ex) {
                    log.info(ex.getMessage());
                }
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getLocalizedMessage());
            }
        }
        
        @HystrixCommand(fallbackMethod = "defaultGetGameByNameFallback")
        @Cacheable(value="getGameByNameCache", key = "#p0")
        public GenericResponseDTO<List<GameDTO>> getGameByName(GetListRequestDTO dto){
            try {
		//Optional<Game> data = repository.findByGameName(gameName);
		Pageable pageable = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("gameCreational.createdDate").descending());
                String gameName = String.valueOf(dto.getParams().get("gameName"));
                Page<Game> datas = repository.findAllByGameName(gameName, pageable);
                if (datas == null) {
                    log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", "204", "DATA NOT FOUND")));
                    return new GenericResponseDTO().noDataFoundResponse();
                }
                log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Game", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
                return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
            } catch (Exception e) {
		log.info(e.getMessage());
		try {
                    log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", 
                                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
                } catch (JsonProcessingException ex) {
                    log.info(ex.getMessage());
                }
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getLocalizedMessage());
            }
        }
	
	@HystrixCommand(fallbackMethod = "defaultPostGameFallback")
        @Caching(
        evict = {
            @CacheEvict(value = "getGameByIDCache", key="#dto.gameID"), //data di cache terupdate
            @CacheEvict(value = "getGameAllCache", allEntries= true) 
        })
        public GenericResponseDTO<GameDTO> postGame(GameDTO dto) {
	try {
            Optional<Game> data = repository.findOneByGameID(dto.getGameID());
            if (data.isPresent()) {
                log.info(objectMapper.writeValueAsString(
                LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", 
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getGameID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getGameID()));
            }
            repository.save(assembler.toDomain(dto));
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Game", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(dto);
	} catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", 
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

    @HystrixCommand(fallbackMethod = "defaultPutGameFallback")
    @Caching(
    evict = {
      		@CacheEvict(value = "getGameByIDCache", key="#dto.gameID"),
                @CacheEvict(value = "getGamekAllCache", allEntries= true)
            
    })
    public GenericResponseDTO<GameDTO> putGame(GameUpdateDTO dto) {
	try {
            Optional<Game> data = repository.findOneByGameID(dto.getGameID());
            if (!data.isPresent()) {
    		log.info(objectMapper.writeValueAsString(
                LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", 
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, 
                        dto.getGameID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getGameID()));
            }         
            data.get().setGenre(dto.getGenre());
            data.get().setGameName(dto.getGameName());
            data.get().setPlatform(dto.getPlatform());
            data.get().setQuantity(dto.getQuantity());
            data.get().getGameCreational().setModifiedBy(dto.getRequestBy() == null ? "SYSTEM" 
                : dto.getRequestBy() == null? "SYSTEM": dto.getRequestBy());
            data.get().getGameCreational().setModifiedDate(dto.getRequestDate() == null? (new Date())
                .toInstant().atZone(ZoneId.systemDefault()): dto.getRequestDate().toInstant().atZone(ZoneId.systemDefault()));
	 
            repository.save(data.get());
    		
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Game", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
	} catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", 
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
	
    @HystrixCommand(fallbackMethod = "defaultDeleteGameFallback")
    @Caching(
    evict = {
        	@CacheEvict(value = "getGameByIDCache", key="#dto.referenceID"),
                @CacheEvict(value = "getGameAllCache", allEntries= true)
    })
    public GenericResponseDTO<GameDTO> deleteGame(DeleteRequestDTO dto) {
	try {
            Optional<Game> data = repository.findOneByGameID(dto.getReferenceID());
            if (!data.isPresent()) {
                log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                        MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()));
            }
            repository.delete(data.get());
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Game", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
	} catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Game", new Date(), "Rest", 
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), 
                e.getLocalizedMessage());
	}
    }
		
    public GenericResponseDTO<List<GameDTO>> defaultGetGameAllFallback(GetListRequestDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<List<GameDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data"  
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<GameDTO> defaultGetGameByIDFallback(String gameID, Throwable e) throws IOException {
	return new GenericResponseDTO<GameDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
    
    public GenericResponseDTO<List<GameDTO>> defaultGetGameByNameFallback(GetListRequestDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<List<GameDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<GameDTO> defaultPostGameFallback(GameDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<GameDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	    : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<GameDTO> defaultPutGameFallback(GameUpdateDTO dto, Throwable e) throws IOException {
        return new GenericResponseDTO<GameDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
            : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
	
    public GenericResponseDTO<GameDTO> defaultDeleteGameFallback(DeleteRequestDTO dto, Throwable e) throws IOException {
	return new GenericResponseDTO<GameDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	    e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
            : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" 
            : e.getLocalizedMessage());
    }
}
