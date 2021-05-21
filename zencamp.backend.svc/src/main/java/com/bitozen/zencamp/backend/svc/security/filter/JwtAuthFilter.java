package com.bitozen.zencamp.backend.svc.security.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.bitozen.zencamp.backend.svc.security.service.JwtAuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthFilter extends GenericFilterBean {

    /**
     * @param request
     * @param response
     * @param filter
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
        //get authentication
        Authentication auth = JwtAuthService.getInstance().getAuthentication((HttpServletRequest) request);

        //set context holder
        SecurityContextHolder.getContext().setAuthentication(auth);

        // do filter
        filter.doFilter(request, response);
    }


}
