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

import com.bitozen.zencamp.backend.domain.Book;
import com.bitozen.zencamp.backend.domain.repository.BookRepository;
import com.bitozen.zencamp.backend.dto.BookDTO;
import com.bitozen.zencamp.backend.dto.BookUpdateDTO;
import com.bitozen.zencamp.backend.dto.common.DeleteRequestDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.assembler.BookDTOAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;

import lombok.extern.slf4j.Slf4j;

@Service
@PropertySource("classpath:error-message.properties")
@Slf4j
public class BookHystrixService {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private BookDTOAssembler assembler;
	
	@Autowired
	private BookRepository repository;
	
	@Value("${data.integrity.exception.message}")
	private String INTEGRITY_ERROR_MSG;

	@Value("${data.not.found.message}")
	private String DATA_NOT_FOUND_MSG;
	
	@HystrixCommand(fallbackMethod = "defaultGetBookAllFallback")
    @Cacheable(value="getBookAllCache", key = "#p0")
	public GenericResponseDTO<List<BookDTO>> getBookAll(GetListRequestDTO dto) {
		try {
			 Pageable page = PageRequest.of(dto.getOffset(), dto.getLimit(), Sort.by("bookCreational.createdDate").descending());
			 Page<Book> datas = repository.findAll(page);
			 if (datas == null) {
	                log.info(objectMapper.writeValueAsString(
	                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", "204", "DATA NOT FOUND")));
	                 return new GenericResponseDTO().noDataFoundResponse();
	            }
	            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
	                    ProjectType.CRUD, "Book", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
	                    new GenericResponseDTO().successResponse().getMessage())));
	            return new GenericResponseDTO().successResponse(assembler.toDTOs(datas.getContent()));
		} catch (Exception e) {
   		log.info(e.getMessage());
   		try {
               log.info(objectMapper.writeValueAsString(
                       LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
           } catch (JsonProcessingException ex) {
               log.info(ex.getMessage());
           }
           return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getLocalizedMessage());
		}
	}
	
	@HystrixCommand(fallbackMethod = "defaultGetBookByIDFallback")
    @Cacheable(value="getBookByIDCache", key = "#p0")
public GenericResponseDTO<BookDTO> getBookByID(String bookID) {
	try {
		Optional<Book> data = repository.findOneByBookID(bookID);
		if (data == null) {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", "204", "DATA NOT FOUND")));
             return new GenericResponseDTO().noDataFoundResponse();
        }
        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Book", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                new GenericResponseDTO().successResponse().getMessage())));
        return new GenericResponseDTO().successResponse(assembler.toDTO(data.get()));
	} catch (Exception e) {
		log.info(e.getMessage());
		try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getLocalizedMessage());
	}
}
	
	@HystrixCommand(fallbackMethod = "defaultPostBookFallback")
    @Caching(
        evict = {
            @CacheEvict(value = "getBookByIDCache", key="#dto.bookID"),
            @CacheEvict(value = "getBookAllCache", allEntries= true)
        })
public GenericResponseDTO<BookDTO> postBook(BookDTO dto) {
	try {
		Optional<Book> data = repository.findOneByBookID(dto.getBookID());
		if (data.isPresent()) {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getBookID()))));
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getBookID()));
        }
        repository.save(assembler.toDomain(dto));
        log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                ProjectType.CRUD, "Book", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                new GenericResponseDTO().successResponse().getMessage())));
        return new GenericResponseDTO().successResponse(dto);
	} catch (Exception e) {
        log.info(e.getMessage());
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
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
	
	@HystrixCommand(fallbackMethod = "defaultPutBookFallback")
    @Caching(
        evict = {
        		@CacheEvict(value = "getBookByIDCache", key="#dto.bookID"),
                @CacheEvict(value = "getBookAllCache", allEntries= true)
            
        })
public GenericResponseDTO<BookDTO> putBook(BookUpdateDTO dto) {
	 try {
		 Optional<Book> data = repository.findOneByBookID(dto.getBookID());
		 if (!data.isPresent()) {
    			log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getBookID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getBookID()));
    		}
                     
		 data.get().setBookTitle(dto.getBookTitle());
		 data.get().setBookGenre(dto.getBookGenre());
		 data.get().setReleaseDate(dto.getReleaseDate());
		 data.get().setBookNotes(dto.getBookNotes());
		 data.get().getBookCreational().setModifiedBy(dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy() == null? "SYSTEM": dto.getRequestBy());
		 data.get().getBookCreational().setModifiedDate(dto.getRequestDate() == null? (new Date()).toInstant().atZone(ZoneId.systemDefault()): dto.getRequestDate().toInstant().atZone(ZoneId.systemDefault()));
	 
		 repository.save(data.get());
    		
    		log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Book", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
	 } catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
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
	
	@HystrixCommand(fallbackMethod = "defaultDeleteBookFallback")
    @Caching(
        evict = {
        		@CacheEvict(value = "getBookByIDCache", key="#dto.referenceID"),
                @CacheEvict(value = "getBookAllCache", allEntries= true)
            
        })
	public GenericResponseDTO<BookDTO> deleteBook(DeleteRequestDTO dto) {
	 try {
		 Optional<Book> data = repository.findOneByBookID(dto.getReferenceID());
		 if (!data.isPresent()) {
    			log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()))));
                return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), MessageFormat.format(INTEGRITY_ERROR_MSG, dto.getReferenceID()));
    		}
		 repository.delete(data.get());
    		
    		log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CRUD, "Book", new Date(), "Rest", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse();
	 } catch (Exception e) {
		 log.info(e.getMessage());
    		try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CRUD, "Book", new Date(), "Rest", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), e.getLocalizedMessage());
	 }
	}
	
	public GenericResponseDTO<List<BookDTO>> defaultGetBookAllFallback(GetListRequestDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<List<BookDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	}
	
	public GenericResponseDTO<BookDTO> defaultGetBookByIDFallback(String bookID, Throwable e) throws IOException {
		 return new GenericResponseDTO<BookDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	}
	
	public GenericResponseDTO<BookDTO> defaultPostBookFallback(BookDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<BookDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	}
	
	public GenericResponseDTO<BookDTO> defaultPutBookFallback(BookUpdateDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<BookDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	}
	
	public GenericResponseDTO<BookDTO> defaultDeleteBookFallback(DeleteRequestDTO dto, Throwable e) throws IOException {
		 return new GenericResponseDTO<BookDTO>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
	                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
	                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
	 }
}
