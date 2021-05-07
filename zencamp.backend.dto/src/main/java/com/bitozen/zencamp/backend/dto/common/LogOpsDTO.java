package com.bitozen.zencamp.backend.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author amy
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogOpsDTO {

    private String domain;
    private String domainEvent;
    private String domainClass;
    private String domainParty;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date timeEvent;
    private String eventType;
    private String eventOps;
    private String user;
    private Object data;

}
