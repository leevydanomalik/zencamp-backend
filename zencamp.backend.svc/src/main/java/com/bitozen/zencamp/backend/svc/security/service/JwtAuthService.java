package com.bitozen.zencamp.backend.svc.security.service;

import com.bitozen.zencamp.backend.svc.security.model.JwtAuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JwtAuthService {
    private static JwtAuthService instance = null;

    private static final long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(30); // 30 Minutes
    private static final String SECRET = "secret";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";


    public static JwtAuthService getInstance() {
        if (instance == null) {
            instance = new JwtAuthService();
        }
        return instance;
    }

    /**
     * @param response
     * @param username
     */
    public void addAuthentication(HttpServletResponse response, String username) throws IOException {
        String sJwt = Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(createPayload(sJwt));
        response.setHeader(HEADER_STRING, TOKEN_PREFIX + " " + sJwt);
    }

    /**
     * @param request
     * @return Authentication
     */
    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (!StringUtils.isEmpty(token)) {
            try {
                String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
                return user != null ? new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()) : null;
            }catch(Exception e){
                return null;
            }
        } else {
            return null;
        }
    }

    private String createPayload(String jwt) {
        ObjectMapper mapper = new ObjectMapper();
        String payload = "";
        try {
            payload = mapper.writeValueAsString(JwtAuthResponse.builder()
                    .accessToken(TOKEN_PREFIX + " " + jwt)
                    .type(TOKEN_PREFIX)
                    .build()
            );
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return payload;
    }
}
