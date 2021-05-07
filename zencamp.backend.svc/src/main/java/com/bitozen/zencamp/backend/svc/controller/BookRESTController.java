package com.bitozen.zencamp.backend.svc.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitozen.zencamp.backend.common.BookGenre;
import com.bitozen.zencamp.backend.dto.BookDTO;
import com.bitozen.zencamp.backend.dto.BookUpdateDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import com.bitozen.zencamp.backend.dto.common.DeleteRequestDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.hystrix.BookHystrixService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BookRESTController {

	@Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;
    
    @Autowired
    private BookHystrixService service;
    
    @RequestMapping(value = "/get.book.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    BookDTO getDummy() {
    	return new BookDTO(
    			 UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
    			 "Dracula",
    			 BookGenre.HORROR,
    			 new Date(),
    			 "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry.",
    			 new CreationalSpecificationDTO().getInstance()
    			);
    }
    
    @RequestMapping(value = "/get.book.all",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<List<BookDTO>>> getBookAll(@RequestBody GetListRequestDTO dto){
    	try{
    		log.info(objectMapper.writeValueAsString(
					LogOpsUtil.getLogOps(ProjectType.CRUD, "Book", BookRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Get All Book Complete",
                            "SYSTEM",
                            dto)));
    	} catch(JsonProcessingException ex) {
			log.info(ex.getMessage());
		}
    	GenericResponseDTO<List<BookDTO>> response = service.getBookAll(dto);
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/get.book.by.id/{bookID}",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<BookDTO>> getBookByID(@PathVariable("bookID") String bookID){
    	try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Book", BookRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Get Book by ID",
                            "SYSTEM",
                            bookID)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<BookDTO> response = service.getBookByID(bookID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/post.book",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<BookDTO>> postBook(@RequestBody BookDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Book", BookRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Create",
                            dto.getBookCreationalDTO() == null ? "SYSTEM" : dto.getBookCreationalDTO().getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<BookDTO> response = service.postBook(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/update.book",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<BookDTO>> putBook(@RequestBody BookUpdateDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Book", BookRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Update",
                            dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<BookDTO> response = service.putBook(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/delete.book",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<BookDTO>> deleteBook(@RequestBody DeleteRequestDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Book", BookRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Delete",
                            dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<BookDTO> response = service.deleteBook(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
