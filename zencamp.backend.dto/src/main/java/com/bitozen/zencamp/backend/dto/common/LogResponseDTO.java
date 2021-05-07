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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogResponseDTO {

    private String domain;
    private String domainEvent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "GMT+7")
    private Date timeEvent;
    private String eventOpsStatus;
    private String eventOpsHttp;
    private Object data;

}
