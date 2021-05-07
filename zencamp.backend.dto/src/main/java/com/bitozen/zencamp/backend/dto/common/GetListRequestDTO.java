package com.bitozen.zencamp.backend.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetListRequestDTO implements Serializable {

    private Map<String, Object> params;
    private int offset;
    private int limit;

    @JsonIgnore
    public GetListRequestDTO getInstance(){
        Map<String, Object> param = new HashMap<>();
        param.put("employeeStatus", DataStatus.ACTIVE);
        return new GetListRequestDTO(param, 0, 5);
    }
}
