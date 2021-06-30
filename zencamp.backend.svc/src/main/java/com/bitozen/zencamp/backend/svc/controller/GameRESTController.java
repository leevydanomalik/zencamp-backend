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

import com.bitozen.zencamp.backend.dto.GameDTO;
import com.bitozen.zencamp.backend.dto.GameUpdateDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import com.bitozen.zencamp.backend.dto.common.DeleteRequestDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.hystrix.GameHystrixService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GameRESTController {

	@Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;
    
    @Autowired
    private GameHystrixService service;
    
    /*@RequestMapping(value = "/get.book.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    GameDTO getDummy() {
    	return new BookDTO(
    			 UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
    			 "Dracula",
    			 BookGenre.HORROR,
    			 new Date(),
    			 "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry.",
    			 new CreationalSpecificationDTO().getInstance()
    			);
    }*/
    
    @RequestMapping(value = "/get.game.all",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<List<GameDTO>>> getGameAll(@RequestBody GetListRequestDTO dto){
    	try{
    		log.info(objectMapper.writeValueAsString(
			LogOpsUtil.getLogOps(ProjectType.CRUD, "Game", GameRESTController.class.getName(),
                        httpRequest.getRequestURL().toString(),
                        new Date(), "Rest", "Get All Game Complete","SYSTEM",dto)));
    	} catch(JsonProcessingException ex) {
			log.info(ex.getMessage());
		}
    	GenericResponseDTO<List<GameDTO>> response = service.getGameAll(dto);
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/get.game.by.id/{gameID}",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<GameDTO>> getGameByID(@PathVariable("gameID") String gameID){
    	try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Game", GameRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Get Game by ID",
                            "SYSTEM",
                            gameID)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<GameDTO> response = service.getGameByID(gameID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/get.game.by.name",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<List<GameDTO>>> getGameByName(@RequestBody GetListRequestDTO dto){
    	try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Game", GameRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Get Game by Name",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<List<GameDTO>> response = service.getGameByName(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/post.game",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<GameDTO>> postGame(@RequestBody GameDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Game", GameRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Create",
                            dto.getGameCreationalDTO() == null ? "SYSTEM" : dto.getGameCreationalDTO().getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<GameDTO> response = service.postGame(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/update.game",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<GameDTO>> putGame(@RequestBody GameUpdateDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Game", GameRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Update",
                            dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<GameDTO> response = service.putGame(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/delete.game",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<GameDTO>> deleteGame(@RequestBody DeleteRequestDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Game", GameRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Delete",
                            dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<GameDTO> response = service.deleteGame(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
