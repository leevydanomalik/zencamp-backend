/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.assembler;
import com.bitozen.zencamp.backend.domain.repository.StoreProcedureRepository;
import com.bitozen.zencamp.backend.dto.StoreProcedureDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dumayangsari
 */
 @Service
@Slf4j
public class StoreProcedureDTOAssembler {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private StoreProcedureRepository repository;
    
    @HystrixCommand(fallbackMethod = "defaultGetMemoAnggaranFallback")
    public GenericResponseDTO<List<StoreProcedureDTO>> getMemoAnggaran() throws ParseException, JsonProcessingException {
        try {
            List<Object[]> memos = repository.findMemoAnggaran();

            List<StoreProcedureDTO> memosDTOs = new ArrayList<>();
            memos.stream().map((Object[] temp) -> {
                StoreProcedureDTO dto = new StoreProcedureDTO();
                dto.setMoid(temp[0] != null ? String.valueOf(temp[0]) : "");
                dto.setMosubdit(temp[1] != null ?  String.valueOf(temp[1]) : "");
               
                return dto;
            }).forEachOrdered((dto) -> {
                memosDTOs.add(dto);
            });

            if (memosDTOs.isEmpty()) {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Training", new java.util.Date(), "Query", "204", "No Data Found")));
                return new GenericResponseDTO().noDataFoundResponse();
            }
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogResponse(
                    ProjectType.CQRS, "Training", new java.util.Date(), "Query", new GenericResponseDTO().successResponse().getCode(),
                    new GenericResponseDTO().successResponse().getMessage())));
            return new GenericResponseDTO().successResponse(memosDTOs);
        } catch (Exception e) {
            log.info(e.getMessage());
            try {
                log.info(objectMapper.writeValueAsString(
                        LogOpsUtil.getErrorResponse(ProjectType.CQRS, "Training", new java.util.Date(), "Query", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getStackTrace())));
            } catch (JsonProcessingException ex) {
                log.info(ex.getMessage());
            }
            return new GenericResponseDTO().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getLocalizedMessage());
        }

    }

    private GenericResponseDTO<List<StoreProcedureDTO>> defaultGetMemoAnggaranFallback(Throwable e) throws IOException {
        return new GenericResponseDTO<List<StoreProcedureDTO>>().errorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                e instanceof HystrixTimeoutException ? "Connection Timeout. Please Try Again Later"
                        : e instanceof HystrixBadRequestException ? "Bad Request. Please recheck submitted data" : e.getLocalizedMessage());
    }
    
}
