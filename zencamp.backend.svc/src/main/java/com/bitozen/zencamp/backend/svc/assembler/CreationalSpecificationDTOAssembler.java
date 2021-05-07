package com.bitozen.zencamp.backend.svc.assembler;

import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component("masterdataCreationalSpecificationDTOAssembler")
public class CreationalSpecificationDTOAssembler implements IObjectAssembler<CreationalSpecification, CreationalSpecificationDTO> {

    @Override
    public CreationalSpecificationDTO toDTO(CreationalSpecification creationalSpecification) {
        return new CreationalSpecificationDTO(creationalSpecification.getCreatedBy() == null? "SYSTEM": creationalSpecification.getCreatedBy(),
                creationalSpecification.getCreatedDate() == null? new Date(): Date.from(creationalSpecification.getCreatedDate().toInstant()),
                creationalSpecification.getModifiedBy(),
                creationalSpecification.getModifiedDate() == null? null: Date.from(creationalSpecification.getModifiedDate().toInstant()));
    }

    @Override
    public CreationalSpecification toDomain(CreationalSpecificationDTO creationalSpecificationDTO) {
        return new CreationalSpecification(
                creationalSpecificationDTO.getCreatedBy()== null? "SYSTEM": creationalSpecificationDTO.getCreatedBy(),
                creationalSpecificationDTO.getCreatedDate()== null? (new Date()).toInstant().atZone(ZoneId.systemDefault()): creationalSpecificationDTO.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()),
                creationalSpecificationDTO.getModifiedBy(),
                creationalSpecificationDTO.getModifiedDate() == null? null: creationalSpecificationDTO.getModifiedDate().toInstant().atZone(ZoneId.systemDefault()));
    }

    @Override
    public List<CreationalSpecificationDTO> toDTOs(Set<CreationalSpecification> set) {
        return null;
    }

    @Override
    public List<CreationalSpecificationDTO> toDTOs(List<CreationalSpecification> list) {
        return null;
    }

    @Override
    public List<CreationalSpecification> toDomains(List<CreationalSpecificationDTO> list) {
        return null;
    }
}
