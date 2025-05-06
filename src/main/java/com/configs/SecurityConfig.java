package com.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.JWT.JWTUtil;
import com.JWT.JwtAuthenticationFilter;
import com.JWT.JwtValidationFilter;
import com.service.CustomUserDetialsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private JWTUtil jwtService;
	
	@Autowired
	private CustomUserDetialsService userDetailsService;
	
	@Autowired
	private ExceptionHandler exceptionHandler;
	
    @Bean
    SecurityFilterChain securityFilterChainConfig(HttpSecurity http, AuthenticationConfiguration config) throws Exception {
    	AuthenticationManager authManager = config.getAuthenticationManager();
    	JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, authManager);
    	JwtValidationFilter jwtValidationFilter = new JwtValidationFilter(jwtService, userDetailsService);
		http.csrf((csrf)->{
			csrf.disable();
		})
		.formLogin(form->form.disable())
		.httpBasic(basic-> basic.disable())
		.authorizeHttpRequests((request)->{
			request.requestMatchers("/studyApp/auth/login").permitAll();
			request.requestMatchers(HttpMethod.POST, "/studyApp/account").permitAll();
			request.anyRequest().authenticated();
		})
		.sessionManagement(management->{
			management.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		})
		.exceptionHandling(handler-> handler.authenticationEntryPoint(exceptionHandler))
		.addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
   
    	return http.build();
	}
}
