package com.bitozen.zencamp.backend.svc.security.filter;

import com.bitozen.zencamp.backend.svc.security.model.UserCredential;
import com.bitozen.zencamp.backend.svc.security.service.JwtAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Ipan Taupik Rahman
 */
public class JwtAuthLoginFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * @param url
     * @param authManager
     */
    public JwtAuthLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    /**
     * @param request
     * @param response
     * @return Authentication
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        UserCredential credential = new ObjectMapper().readValue(request.getInputStream(), UserCredential.class);

        // do auth and return data with authorities
        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        credential.getUsername(),
                        credential.getPassword(),
                        Collections.emptyList()
                ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        JwtAuthService.getInstance().addAuthentication(response, authResult.getName());
    }
}
