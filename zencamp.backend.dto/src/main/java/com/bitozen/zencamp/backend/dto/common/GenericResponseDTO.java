package com.bitozen.zencamp.backend.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author amy
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseDTO<T> implements Serializable {

    private ResponseStatus status;
    private String code;
    private String message;
    private T data;

    @JsonIgnore
    public GenericResponseDTO<T> successResponse(){
        GenericResponseDTO<T> data = new GenericResponseDTO<T>();
        data.setStatus(ResponseStatus.S);
        data.setCode("201");
        data.setMessage("Process Successed");
        return data;
    }

    @JsonIgnore
    public GenericResponseDTO<T> successResponse(T t){
        GenericResponseDTO<T> data = new GenericResponseDTO<T>();
        data.setStatus(ResponseStatus.S);
        data.setCode("201");
        data.setData(t);
        data.setMessage("Process Successed");
        return data;
    }

    @JsonIgnore
    public GenericResponseDTO<T> noDataFoundResponse(){
        GenericResponseDTO<T> data = new GenericResponseDTO<T>();
        data.setStatus(ResponseStatus.S);
        data.setCode("204");
        data.setMessage("No Data Found");
        return data;
    }

    @JsonIgnore
    public GenericResponseDTO<T> errorResponse(String code, String message){
        GenericResponseDTO<T> data = new GenericResponseDTO<T>();
        data.setStatus(ResponseStatus.F);
        data.setCode(code);
        data.setMessage(message);
        return data;
    }
}
