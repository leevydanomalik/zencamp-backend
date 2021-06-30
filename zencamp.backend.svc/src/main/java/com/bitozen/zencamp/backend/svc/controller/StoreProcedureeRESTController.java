/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.controller;

import com.bitozen.zencamp.backend.dto.StoreProcedureDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.assembler.StoreProcedureDTOAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dumayangsari
 */
@RestController
@Slf4j
public class StoreProcedureeRESTController {

    @Autowired
    private StoreProcedureDTOAssembler memoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;

    @GetMapping(value = "/get.training.employee.history")
    public ResponseEntity<GenericResponseDTO<List<StoreProcedureDTO>>> getMemoAnggaran(
            //@PathVariable("employeeID") String employeeID
    ) throws ParseException, JsonProcessingException {
        try {
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogOps(ProjectType.CQRS, "Training", StoreProcedureeRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Query", "getMemoAnggaran",
                            "SYSTEM",
                            "")));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<List<StoreProcedureDTO>> response = memoService.getMemoAnggaran();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

