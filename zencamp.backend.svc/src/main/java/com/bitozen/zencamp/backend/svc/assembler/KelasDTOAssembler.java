package com.bitozen.zencamp.backend.svc.assembler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bitozen.zencamp.backend.domain.Kelas;
import com.bitozen.zencamp.backend.domain.common.CreationalSpecification;
import com.bitozen.zencamp.backend.domain.repository.KelasRepository;
import com.bitozen.zencamp.backend.dto.KelasDTO;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KelasDTOAssembler implements IObjectAssembler<Kelas, KelasDTO> {

	@Autowired
	private KelasRepository repository;
	
	@Autowired
    private CreationalSpecificationDTOAssembler creationalAssembler;

	@Autowired
	private SiswaDTOAssembler siswaDTOAssembler;
	
	public KelasDTO toDTO(Kelas domainObject) {
			 	KelasDTO dto = new KelasDTO();
	            dto.setKelasID(domainObject.getKelasID());
	            dto.setKelasName(domainObject.getKelasName());
	            dto.setKelasDescription(domainObject.getKelasDescription());
	            dto.setKelasYear(domainObject.getKelasYear());
	            dto.setListSiswaDTO(domainObject.getListSiswa() == null
	                    ? new ArrayList<>()
	                    : siswaDTOAssembler.toDTOs(domainObject.getListSiswa()));
	            dto.setKelasCreationalDTO(domainObject.getKelasCreational() == null
	                    ? new CreationalSpecificationDTO().getInstance()
	                    : creationalAssembler.toDTO(domainObject.getKelasCreational()));
	            return dto;
	}

	public Kelas toDomain(KelasDTO dtoObject) {
		 	return new Kelas(
		 			dtoObject.getKelasID(),
		 			dtoObject.getKelasName(),
		 			dtoObject.getKelasDescription(),
		 			dtoObject.getKelasYear(),
		 			dtoObject.getListSiswaDTO() == null
		 				? new HashSet<>()
		 				: siswaDTOAssembler.toSetDomains(dtoObject.getListSiswaDTO()),
		 			dtoObject.getKelasCreationalDTO() == null
                    ? new CreationalSpecification().getInstance()
                    : creationalAssembler.toDomain(dtoObject.getKelasCreationalDTO())
		 			);

	}

	public List<KelasDTO> toDTOs(Set<Kelas> set) {
		List<KelasDTO> datas = new ArrayList<>();
        set.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	public List<KelasDTO> toDTOs(List<Kelas> list) {
		List<KelasDTO> datas = new ArrayList<>();
        list.stream().forEach((o) -> {
            datas.add(toDTO(o));
        });
        return datas;
	}

	public List<Kelas> toDomains(List<KelasDTO> list) {
		List<Kelas> datas = new ArrayList<>();
        list.stream().forEach((o) -> {
            datas.add(toDomain(o));
        });
        return datas;
	}
	
	public Set<Kelas> toSetDomains(List<KelasDTO> list) {
		Set<Kelas> datas = new HashSet<>();
        list.stream().forEach((o) -> {
            datas.add(toDomain(o));
        });
        return datas;
	}

	
}
