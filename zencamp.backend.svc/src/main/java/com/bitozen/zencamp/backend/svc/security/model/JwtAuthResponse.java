package com.bitozen.zencamp.backend.svc.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author Ipan Taupik Rahman
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtAuthResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("type")
    private String type;
}
