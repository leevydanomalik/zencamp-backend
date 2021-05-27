/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.controller;

import com.bitozen.zencamp.backend.dto.CustomerDTO;
import com.bitozen.zencamp.backend.dto.CustomerUpdateDTO;
import com.bitozen.zencamp.backend.dto.common.DeleteRequestDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.hystrix.CustomerHystrixService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jeremia
 */
@RestController
@Slf4j
public class CustomerRESTController {
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private HttpServletRequest httpRequest;
    
    @Autowired
    private CustomerHystrixService service;
    
    @RequestMapping(value = "/get.customer.all",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<List<CustomerDTO>>> getCustomerAll(@RequestBody GetListRequestDTO dto){
    	try{
    		log.info(objectMapper.writeValueAsString(
					LogOpsUtil.getLogOps(ProjectType.CRUD, "Customer", CustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Get All Customer Complete",
                            "SYSTEM",
                            dto)));
    	} catch(JsonProcessingException ex) {
			log.info(ex.getMessage());
		}
    	GenericResponseDTO<List<CustomerDTO>> response = service.getCustomerAll(dto);
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/post.customer",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<CustomerDTO>> postCustomer(@RequestBody CustomerDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Customer", CustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Create",
                            dto.getCustomercreationalDTO() == null ? "SYSTEM" : dto.getCustomercreationalDTO().getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<CustomerDTO> response = service.postCustomer(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @RequestMapping(value = "/get.customer.by.id/{customerid}",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<CustomerDTO>> getCustomerByID(@PathVariable("customerid") String customerid){
    	try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Customer", CustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Get Customer by ID",
                            "SYSTEM",
                            customerid)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<CustomerDTO> response = service.getCustomerByID(customerid);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/get.customer.by.name",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<List<CustomerDTO>>> getCustomerByFullname(@RequestBody GetListRequestDTO dto){
    	try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Customer", CustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Get Customer by Name",
                            "SYSTEM",
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<List<CustomerDTO>> response = service.getCustomerByFullname(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    
    
    @RequestMapping(value = "/update.customer",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<CustomerDTO>> putCustomer(@RequestBody CustomerUpdateDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Customer", CustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Update",
                            dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<CustomerDTO> response = service.putCustomer(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/delete.customer",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<CustomerDTO>> deleteCustomer(@RequestBody DeleteRequestDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(
                    LogOpsUtil.getLogOps(ProjectType.CRUD, "Customer", CustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Delete",
                            dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<CustomerDTO> response = service.deleteCustomer(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
