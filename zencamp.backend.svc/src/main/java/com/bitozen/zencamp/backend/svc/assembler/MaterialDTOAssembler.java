/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitozen.zencamp.backend.svc.assembler;

import com.bitozen.zencamp.backend.domain.Material;
import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import com.bitozen.zencamp.backend.domain.repository.MaterialRepository;
import com.bitozen.zencamp.backend.dto.MaterialDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dumayangsari
 */
@Component
@Slf4j
public class MaterialDTOAssembler implements IObjectAssembler<Material, MaterialDTO>{
    @Autowired
    private MaterialRepository repository;
    
    @Autowired
    private CreationalSpecificationDTOAssembler creationalAssembler;
    
    @Override
	public MaterialDTO toDTO(Material domainObject) {
            return new MaterialDTO(
		domainObject.getMaterialID(),
		domainObject.getMaterialName(),
                domainObject.getMaterialKIMAP(),
                domainObject.getMaterialDesc(),
		domainObject.getMaterialType(),
                domainObject.getMaterialUoM(),
                domainObject.getMaterialPrice(),
                domainObject.getMaterialQty(),
                    
		domainObject.getMaterialCreational() == null ? new CreationalSpecificationDTO().getInstance() 
                        : creationalAssembler.toDTO(domainObject.getMaterialCreational())
            );
	}
    @Override
	public Material toDomain(MaterialDTO dtoObject) {
            return new Material(
		dtoObject.getMaterialID(),
		dtoObject.getMaterialName(),
                dtoObject.getMaterialKIMAP(),
                dtoObject.getMaterialDesc(),
		dtoObject.getMaterialType(),
                dtoObject.getMaterialUoM(),
                dtoObject.getMaterialPrice(),
                dtoObject.getMaterialQty(),
                    
		dtoObject.getMaterialCreationalDTO() == null ? new CreationalSpecification().getInstance() 
                        : creationalAssembler.toDomain(dtoObject.getMaterialCreationalDTO())
            );
	}
        
    @Override
	public List<MaterialDTO> toDTOs(Set<Material> domainObjects) {
		List<MaterialDTO> datas = new ArrayList<>();
            domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

    @Override
	public List<MaterialDTO> toDTOs(List<Material> domainObjects) {
		List<MaterialDTO> datas = new ArrayList<>();
            domainObjects.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

    @Override
	public List<Material> toDomains(List<MaterialDTO> dtoObjects) {
		return null;
	}
}
