package com.bitozen.zencamp.backend.svc.assembler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bitozen.zencamp.backend.domain.Siswa;
import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import com.bitozen.zencamp.backend.domain.repository.SiswaRepository;
import com.bitozen.zencamp.backend.dto.KelasDTO;
import com.bitozen.zencamp.backend.dto.SiswaDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SiswaDTOAssembler implements IObjectAssembler<Siswa, SiswaDTO>{

	@Autowired
	private SiswaRepository repository;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
    private CreationalSpecificationDTOAssembler creationalAssembler;
	
	public SiswaDTO toDTO(Siswa domainObject) {
			SiswaDTO dto = new SiswaDTO();
			dto.setSiswaID(domainObject.getSiswaID());
			dto.setSiswaName(domainObject.getSiswaName());
			dto.setSiswaDateOfBirth(domainObject.getSiswaDateOfBirth());
			 dto.setSiswaCreationalDTO(domainObject.getSiswaCreational() == null
             ? new CreationalSpecificationDTO().getInstance()
             : creationalAssembler.toDTO(domainObject.getSiswaCreational()));
			 return dto;
	}

	public Siswa toDomain(SiswaDTO dtoObject) {
			return new Siswa(
                    dtoObject.getSiswaID(),
                    dtoObject.getSiswaName(),
                    dtoObject.getSiswaDateOfBirth(),
                    dtoObject.getSiswaCreationalDTO() == null?
                            new CreationalSpecification().getInstance():
                            	creationalAssembler.toDomain(dtoObject.getSiswaCreationalDTO()));

	}

	public List<SiswaDTO> toDTOs(Set<Siswa> set) {
		List<SiswaDTO> datas = new ArrayList<>();
        set.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	public List<SiswaDTO> toDTOs(List<Siswa> list) {
		List<SiswaDTO> datas = new ArrayList<>();
        list.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	public List<Siswa> toDomains(List<SiswaDTO> list) {
		List<Siswa> datas = new ArrayList<>();
        list.stream().forEach((o) -> {
            datas.add(toDomain(o));
        });
        return datas;
	}
	
	public Set<Siswa> toSetDomains(List<SiswaDTO> list) {
		Set<Siswa> datas = new HashSet<>();
        list.stream().forEach((o) -> {
            datas.add(toDomain(o));
        });
        return datas;
	}

	
}
