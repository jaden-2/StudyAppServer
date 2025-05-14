package com.configs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
		.cors(cors-> cors.configurationSource(configurationSource()))
		.formLogin(form->form.disable())
		.httpBasic(basic-> basic.disable())
		.authorizeHttpRequests((request)->{
			request.requestMatchers("/api/studyApp/auth/login").permitAll();
			request.requestMatchers(HttpMethod.POST, "/api/studyApp/account").permitAll();
			request.requestMatchers("/ws-studyApp").permitAll();
			request.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
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
    
    @Bean
    CorsConfigurationSource configurationSource() {
  	  CorsConfiguration config = new CorsConfiguration();
  	  config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
  	  config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
  	  config.setAllowedHeaders(Arrays.asList("*"));
  	  config.setAllowCredentials(true);
  	  
  	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
  	  source.registerCorsConfiguration("/**", config);
  	  return source;
    }
}
