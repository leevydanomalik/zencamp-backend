package com.bitozen.zencamp.backend.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRequestDTO implements Serializable {

    private String referenceID;
    private String requestBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date requestDate;

    @JsonIgnore
    public DeleteRequestDTO getInstance(){
        return new DeleteRequestDTO(UUID.randomUUID().toString().substring(0,8), "SYSTEM", new Date());
    }

}
