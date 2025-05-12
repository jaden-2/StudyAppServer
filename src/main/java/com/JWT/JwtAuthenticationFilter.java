package com.JWT;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.DTO.UserRequest;
import com.entities.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final JWTUtil jwtService;
	private final AuthenticationManager authManager;
	
	ObjectMapper mapper = new ObjectMapper();
	
	public JwtAuthenticationFilter(JWTUtil service, AuthenticationManager manager) {
		this.jwtService = service;
		this.authManager = manager;
		
		this.setFilterProcessesUrl("/api/studyApp/auth/login");
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			UserRequest loginDto = mapper.readValue(request.getInputStream(), UserRequest.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword());
			return authManager.authenticate(authToken);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
		String token = jwtService.generateToken(userDetails);
		
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("{\n" + "\"token\":" + "\"" + token + "\"}");
		response.getWriter().flush();
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().write("{\"message\" : \"Bad credential\"}");
		response.getWriter().flush();
	}
	
	
	

}
