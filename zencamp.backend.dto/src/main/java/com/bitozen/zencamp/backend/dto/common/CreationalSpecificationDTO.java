package com.bitozen.zencamp.backend.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreationalSpecificationDTO implements Serializable {

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdDate;
    private String modifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date modifiedDate;

    @JsonIgnore
    public CreationalSpecificationDTO getInstance() {
        CreationalSpecificationDTO creationalSpecificationDTO = new CreationalSpecificationDTO();
        creationalSpecificationDTO.setCreatedDate(new Date());
        creationalSpecificationDTO.setCreatedBy("SYSTEM");
        return creationalSpecificationDTO;
    }
}
