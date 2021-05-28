package com.bitozen.zencamp.backend.dto;

import java.io.Serializable;
import java.util.List;

import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KelasDTO implements Serializable {

	private String kelasID;
	private String kelasName;
	private String kelasDescription;
	private String kelasYear;
	private List<SiswaDTO> listSiswaDTO;
	private CreationalSpecificationDTO kelasCreationalDTO;
	
	
}
