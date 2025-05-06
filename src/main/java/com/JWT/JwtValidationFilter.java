package com.JWT;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.entities.CustomUserDetails;
import com.service.CustomUserDetialsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends OncePerRequestFilter{
	
	
	private final JWTUtil jwtService;
	private final CustomUserDetialsService userService;
	
	public JwtValidationFilter(JWTUtil service, CustomUserDetialsService userDetailsService) {
		this.jwtService = service;
		this.userService = userDetailsService;
	}
	/*
	 * Intercepts requests, checks the header for JWT token for user authorisation
	 * If no token, it sends client request to the next filter*/
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String header = request.getHeader("Authorization");
		/*
		 * Checks of header is null or does not contain a token that starts with 'Bearer'*/
		if(header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		// Extracts JWT token from a valid header
		String jwtToken = header.substring(7);
		
		String username = jwtService.extractUsername(jwtToken);
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			CustomUserDetails userDetails = userService.loadUserByUsername(username);
			
			if(jwtService.isTokenValid(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null);
				
				authToken.setDetails(new WebAuthenticationDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
