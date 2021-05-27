/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.assembler;

import com.bitozen.zencamp.backend.domain.Customer;
import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import com.bitozen.zencamp.backend.domain.repository.CustomerRepository;
import com.bitozen.zencamp.backend.dto.CustomerDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jeremia
 */
@Component
@Slf4j
public class CustomerDTOAssembler implements IObjectAssembler<Customer, CustomerDTO> {
    
    @Autowired
    private CustomerRepository repository;
    
    @Autowired
    private CreationalSpecificationDTOAssembler creationalAssembler;
    
    @Override
    public CustomerDTO toDTO(Customer domainObject){
        return new CustomerDTO(
                domainObject.getCustomerid(),
                domainObject.getPhonenumber(),
                domainObject.getFullname(),
                domainObject.getOrderto(),
                domainObject.getOrderfrom(),
                domainObject.getCustomercreational() == null ? new CreationalSpecificationDTO().getInstance() : creationalAssembler.toDTO(domainObject.getCustomercreational())
        );
    }
    @Override
    public Customer toDomain(CustomerDTO dtoObject){
        return new Customer(
                dtoObject.getCustomerid(),
                dtoObject.getPhonenumber(),
                dtoObject.getFullname(),
                dtoObject.getOrderto(),
                dtoObject.getOrderfrom(),
                dtoObject.getCustomercreationalDTO() == null ? new CreationalSpecification().getInstance() : creationalAssembler.toDomain(dtoObject.getCustomercreationalDTO())
        );
    }
    
    @Override
    public List<CustomerDTO> toDTOs(Set<Customer> domainObjects){
        List<CustomerDTO> datas = new ArrayList<>();
        domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
    }
    
    @Override
    public List<CustomerDTO> toDTOs(List<Customer> domainObjects) {
	List<CustomerDTO> datas = new ArrayList<>();
        domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
    }
    
    @Override
    public List<Customer> toDomains(List<CustomerDTO> dtoObjects) {
	
	return null;
    }
 
}
