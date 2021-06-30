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
public class GameUpdateDTO implements Serializable {

	private String gameID;
	private String genre;
        private String quantity;
        private String gameName;
        private String platform;
        
        private String requestBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date requestDate;
	
}
