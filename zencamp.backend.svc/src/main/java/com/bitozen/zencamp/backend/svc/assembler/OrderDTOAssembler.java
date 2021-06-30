/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.assembler;

import com.bitozen.zencamp.backend.domain.OrderCustomer;
import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import com.bitozen.zencamp.backend.domain.repository.OrderRepository;
import com.bitozen.zencamp.backend.dto.OrderDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dumayangsari
 */
@Component
@Slf4j
public class OrderDTOAssembler implements IObjectAssembler<OrderCustomer, OrderDTO>{
    @Autowired
    private OrderRepository repository;
    
    @Autowired
    private CreationalSpecificationDTOAssembler creationalAssembler;

    @Override
    public OrderDTO toDTO(OrderCustomer domainObject){
        return new OrderDTO(
                domainObject.getCustomerID(),
                domainObject.getCustomerName(),
                domainObject.getNoTelp(),
                domainObject.getAlamatAsal(),
                domainObject.getAlamatTujuan(),
                domainObject.getMaterialKIMAP(),
                domainObject.getMaterialName(),
                domainObject.getMaterialDesc(),
                domainObject.getMaterialType(),
                domainObject.getMaterialUoM(),
                domainObject.getMaterialPrice(),
                domainObject.getOrderQty(),
                domainObject.getOrderDate(),
                domainObject.getCustomerCreational()==null?new CreationalSpecificationDTO().getInstance()
                        : creationalAssembler.toDTO(domainObject.getCustomerCreational())
        );
    }
    
    @Override
    public OrderCustomer toDomain(OrderDTO dtoObject){
        return new OrderCustomer(
                dtoObject.getCustomerID(),
                dtoObject.getCustomerName(),
                dtoObject.getNoTelp(),
                dtoObject.getAlamatAsal(),
                dtoObject.getAlamatTujuan(),
                dtoObject.getMaterialKIMAP(),
                dtoObject.getMaterialName(),
                dtoObject.getMaterialDesc(),
                dtoObject.getMaterialType(),
                dtoObject.getMaterialUoM(),
                dtoObject.getMaterialPrice(),
                dtoObject.getOrderQty(),
                dtoObject.getOrderDate(),
                dtoObject.getCustomerCreationalDTO()==null?new CreationalSpecification().getInstance()
                        : creationalAssembler.toDomain(dtoObject.getCustomerCreationalDTO())
        );
    }
    
    @Override
	public List<OrderDTO> toDTOs(Set<OrderCustomer> domainObjects) {
		List<OrderDTO> datas = new ArrayList<>();
            domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	@Override
	public List<OrderDTO> toDTOs(List<OrderCustomer> domainObjects) {
		List<OrderDTO> datas = new ArrayList<>();
            domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	@Override
	public List<OrderCustomer> toDomains(List<OrderDTO> dtoObjects) {
		return null;
	}
    
}
