package com.bitozen.zencamp.backend.svc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bitozen.zencamp.backend.dto.KelasDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.hystrix.KelasHystrixService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@PropertySource("classpath:error-message.properties")
@Slf4j
public class KelasRESTController {

	@Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;
    
    @Autowired
    private KelasHystrixService service;
    
    @RequestMapping(value = "/get.kelas.dummy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    KelasDTO getDummy() {
    	return new KelasDTO(
    			UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
   			 	"1-A",
   			 	"Deskripsi Kelas",
   			 	"2007",
   			 	new ArrayList<>(),
   			 	new CreationalSpecificationDTO().getInstance()
    			);
    }
    
    @RequestMapping(value = "/get.kelas.all",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<List<KelasDTO>>> getKelasAll(@RequestBody GetListRequestDTO dto){
    	try{
    		log.info(objectMapper.writeValueAsString(
					LogOpsUtil.getLogOps(ProjectType.CRUD, "Kelas", KelasRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Get All Kelas Complete",
                            "SYSTEM",
                            dto)));
    	} catch(JsonProcessingException ex) {
			log.info(ex.getMessage());
		}
    	GenericResponseDTO<List<KelasDTO>> response = service.getKelasAll(dto);
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/post.kelas",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<KelasDTO>> postKelas(@RequestBody KelasDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Kelas", KelasRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Create",
                            dto.getKelasCreationalDTO() == null ? "SYSTEM" : dto.getKelasCreationalDTO().getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<KelasDTO> response = service.postKelas(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
