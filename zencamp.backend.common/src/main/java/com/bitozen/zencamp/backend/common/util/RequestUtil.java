package com.bitozen.zencamp.backend.common.util;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component("zencampRequestUtil")
@PropertySource("classpath:zencamp.backend.token.properties")
public class RequestUtil {

    private final String HEADER_KEY = "Authorization";

    @Value("${zencamp.backend.token}")
    private String ZENCAMP_TOKEN;

    public HttpEntity<String> getPreFormattedRequestWithToken() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.add(HEADER_KEY, ZENCAMP_TOKEN);

        return new HttpEntity<>(httpHeaders);
    }

}
