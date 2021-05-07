package com.bitozen.wms.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 *
 * @author bitozen
 */
@Component()
@PropertySource("classpath:wms.common.token.properties")
public class CommonRequestUtil {

    private final String HEADER_KEY = "Authorization";

    @Value("${wms.common.token}")
    private String TRX_TOKEN;

    public HttpEntity<String> getPreFormattedRequestWithToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.add(HEADER_KEY, TRX_TOKEN);
        return new HttpEntity<>(httpHeaders);
    }

}
