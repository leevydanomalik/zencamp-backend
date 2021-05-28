package com.bitozen.zencamp.backend.dto;

import java.io.Serializable;
import java.util.Date;

import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SiswaDTO implements Serializable {

	private String siswaID;
	private String siswaName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date siswaDateOfBirth;
	private CreationalSpecificationDTO siswaCreationalDTO;
}
