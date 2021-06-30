/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.controller;

import com.bitozen.zencamp.backend.dto.OrderDTO;
import com.bitozen.zencamp.backend.dto.OrderUpdateDTO;
import com.bitozen.zencamp.backend.dto.common.DeleteRequestDTO;
import com.bitozen.zencamp.backend.dto.common.GenericResponseDTO;
import com.bitozen.zencamp.backend.dto.common.GetListRequestDTO;
import com.bitozen.zencamp.backend.dto.common.LogOpsUtil;
import com.bitozen.zencamp.backend.dto.common.ProjectType;
import com.bitozen.zencamp.backend.svc.hystrix.OrderHystrixService;
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
 * @author Dumayangsari
 */
@RestController
@Slf4j
public class OrderCustomerRESTController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpServletRequest httpRequest;
    
    @Autowired
    private OrderHystrixService service;
    
    @RequestMapping(value = "/get.orderCustomer.all",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GenericResponseDTO<List<OrderDTO>>> getOrderCustomerAll(@RequestBody GetListRequestDTO dto){
        try{
    		log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogOps(ProjectType.CRUD, "OrderCustomer", OrderCustomerRESTController.class.getName(),
                        httpRequest.getRequestURL().toString(),
                        new Date(), "Rest", "Get All Customer Complete","SYSTEM",dto)));
    	} catch(JsonProcessingException ex) {
			log.info(ex.getMessage());
		}
    	GenericResponseDTO<List<OrderDTO>> response = service.getOrderCustomerAll(dto);
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/get.orderCcustomer.by.id/{customerID}",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<OrderDTO>> getOrderCustomerByID(@PathVariable("customerID") String customerID){
    	try {
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogOps(ProjectType.CRUD, "OrderCustomer", OrderCustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Get Customer by ID",
                            "SYSTEM",
                            customerID)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<OrderDTO> response = service.getOrderCustomerByID(customerID);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    /*@RequestMapping(value = "/get.customer.by.name",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<List<CustomerDTO>>> getCustomerByName(@RequestBody GetListRequestDTO dto){
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
        GenericResponseDTO<List<CustomerDTO>> response = service.getCustomerByName(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }*/
    
    @RequestMapping(value = "/post.orderCustomer",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<OrderDTO>> postOrderCustomer(@RequestBody OrderDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogOps(ProjectType.CRUD, "OrderCustomer", OrderCustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Create",
                            dto.getCustomerCreationalDTO() == null ? "SYSTEM" : dto.getCustomerCreationalDTO().getCreatedBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<OrderDTO> response = service.postOrderCustomer(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @RequestMapping(value = "/update.orderCustomer",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<OrderDTO>> putCustomer(@RequestBody OrderUpdateDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogOps(ProjectType.CRUD, "Customer", OrderCustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Update",
                            dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<OrderDTO> response = service.putOrderCustomer(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @RequestMapping(value = "/delete.orderCustomer",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponseDTO<OrderDTO>> deleteOrderCustomer(@RequestBody DeleteRequestDTO dto) {
        try {
            log.info(objectMapper.writeValueAsString(LogOpsUtil.getLogOps(ProjectType.CRUD, "OrderCustomer", OrderCustomerRESTController.class.getName(),
                            httpRequest.getRequestURL().toString(),
                            new Date(), "Rest", "Delete",
                            dto.getRequestBy() == null ? "SYSTEM" : dto.getRequestBy(),
                            dto)));
        } catch (JsonProcessingException ex) {
            log.info(ex.getMessage());
        }
        GenericResponseDTO<OrderDTO> response = service.deleteOrderCustomer(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
