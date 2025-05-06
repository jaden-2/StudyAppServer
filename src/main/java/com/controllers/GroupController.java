package com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.DTO.MessageDTO;
import com.entities.CustomUserDetails;
import com.entities.Message;
import com.entities.User;
import com.service.MessageService;

import jakarta.validation.Valid;

@Controller
public class GroupController {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired 
	private MessageService msgservice;

	public void sendToGroup(String group, Message message) {
		String path = "/studyApp/topic/"+ group;
		messagingTemplate.convertAndSend(path, message);
	}
	
	@MessageMapping("/group/chat")
	public void receiveMessage(@Valid @Payload MessageDTO msg, @AuthenticationPrincipal CustomUserDetails authUser) {
		User sender = authUser.getUser();
		Message message = new Message(sender, msg);
		msgservice.createMessage(message);
		sendToGroup(message.getGroup().getGroupId(), message);
	}
}
