package com.configs;



import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.service.CustomUserDetialsService;


@Configuration
public class ApplicationConfig {
	@Autowired
	private CustomUserDetialsService userDetailsService;
  
	
	@Bean
    PasswordEncoder passwordEncoder () {
    	return new BCryptPasswordEncoder();
    }
  
 
  @Bean
  AuthenticationProvider authenticationProvider() {
	  DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	  authProvider.setUserDetailsService(userDetailsService);
	  authProvider.setPasswordEncoder(passwordEncoder());
	  
	  return authProvider;
  }
 
}
