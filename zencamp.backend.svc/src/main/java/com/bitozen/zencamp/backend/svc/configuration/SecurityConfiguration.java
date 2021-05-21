package com.bitozen.zencamp.backend.svc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.bitozen.zencamp.backend.svc.security.CustomBasicAuthenticationEntryPoint;
import com.bitozen.zencamp.backend.svc.security.filter.JwtAuthFilter;
import com.bitozen.zencamp.backend.svc.security.filter.JwtAuthLoginFilter;

/**
 *
 * @author leevydmalik
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String REALM = "MY_TEST_REALM";
    private static final String SIGNING_KEY = "MaYzkSjmkzPC57L";
    private static final Integer ENCODING_STRENGTH = 256;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
    	 auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
         auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER");
    }    
   
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        .withUser("admin")
        .password("{noop}admin")
        .roles("ADMIN");
    }

    // configuration for swagger
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**");
    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
	        .and().csrf().disable()
	        .authorizeRequests()
	        .antMatchers("/").permitAll()
	        .antMatchers(HttpMethod.POST, "/login").permitAll()
	        .antMatchers("/swagger-ui.html").permitAll()
	        .anyRequest().authenticated()                
	        .and()
	        .addFilterBefore(new JwtAuthLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
	        .addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    
    }
    
    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
        return new CustomBasicAuthenticationEntryPoint();
    }

}