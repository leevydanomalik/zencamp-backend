package com.bitozen.wms.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component()
public class CommonRestClientUtil {

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity restServiceExchange(String url, String uri, HttpMethod httpMethod, HttpEntity request, Class c, Object... varargs) {
        return restTemplate.exchange(url + uri, httpMethod, request, c, varargs);
    }
}
