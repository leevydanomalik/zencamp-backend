package com.bitozen.zencamp.backend.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SiswaCreateDTO implements Serializable {

	private String kelasID;
	private SiswaDTO siswa;
}
