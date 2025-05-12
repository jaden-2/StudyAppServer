package com.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.JWT.JWTUtil;

import com.service.CustomUserDetialsService;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	@Autowired
	private JWTUtil jwtService;	
	@Autowired
	private CustomUserDetialsService userServiceDetails;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("ws-studyApp")
				.setAllowedOriginPatterns("*")
				.addInterceptors(new StompHandshakeInterceptor(jwtService, userServiceDetails))
				.setAllowedOrigins("http://localhost:3000")
				.withSockJS();
			
		WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new SecurityContextChannelInterceptor());
		
		WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/sock/studyApp/topic");
		registry.setApplicationDestinationPrefixes("/sock/studyApp");
		WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
	}

	
}
