package com.bitozen.zencamp.backend.svc.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bitozen.zencamp.backend.dto.SiswaCreateDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.hystrix.SiswaHystrixService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@PropertySource("classpath:error-message.properties")
@Slf4j
public class SiswaRESTController {

	@Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;
    
    @Autowired
    private SiswaHystrixService service;
    
    @RequestMapping(value = "/post.siswa",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<SiswaCreateDTO>> postSiswa(@RequestBody SiswaCreateDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Siswa", SiswaRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Create",
                            dto.getSiswa().getSiswaCreationalDTO() == null ? "SYSTEM" : dto.getSiswa().getSiswaCreationalDTO().getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<SiswaCreateDTO> response = service.postSiswa(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
