package com.configs;

import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextChannelInterceptor implements ChannelInterceptor{

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		Map<String, Object> sessionAttribute = accessor.getSessionAttributes();
		
		if(sessionAttribute != null) {
			SecurityContext context = (SecurityContext)sessionAttribute.get("SPRING_SECURITY_CONTEXT");
			
			if(context != null) {
				SecurityContextHolder.setContext(context);
			}
		}
		return ChannelInterceptor.super.preSend(message, channel);
	}
	
	

}
