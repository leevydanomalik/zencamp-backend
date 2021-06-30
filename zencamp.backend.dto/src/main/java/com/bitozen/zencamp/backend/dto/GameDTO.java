package com.bitozen.zencamp.backend.dto;

import java.io.Serializable;
import com.bitozen.zencamp.backend.dto.common.CreationalSpecificationDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDTO implements Serializable {

	private String gameID;
	private String genre;
        private String quantity;
        private String gameName;
        private String platform;
	private CreationalSpecificationDTO gameCreationalDTO;
	
}
