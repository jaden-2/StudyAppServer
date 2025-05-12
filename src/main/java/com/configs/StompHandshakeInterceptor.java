package com.configs;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.JWT.JWTUtil;
import com.entities.CustomUserDetails;
import com.service.CustomUserDetialsService;

import jakarta.servlet.http.HttpServletRequest;

public class StompHandshakeInterceptor implements HandshakeInterceptor{
	private final JWTUtil jwtService;
	private final CustomUserDetialsService userDetialsService;
	public StompHandshakeInterceptor(JWTUtil service, CustomUserDetialsService userService) {
		this.jwtService = service;
		this.userDetialsService = userService;
	}
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		
		if(request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
			HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
			
			String authHeader = httpServletRequest.getHeader("Authorization");
			
			if(authHeader == null || !authHeader.startsWith("Bearer ")) {
				return false;
			}
			
			String jwtToken = authHeader.substring(7);
			String username = jwtService.extractUsername(jwtToken);
			
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				CustomUserDetails userDetail = userDetialsService.loadUserByUsername(username);
				
				if(jwtService.isTokenValid(jwtToken, userDetail)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetail, null);
					authToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
					
					SecurityContext context = SecurityContextHolder.createEmptyContext();
					context.setAuthentication(authToken);
					SecurityContextHolder.setContext(context);
					
					attributes.put("SPRING_SECURITY_CONTEXT", context);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}



}
