package com.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.DTOS.MessageDTO;
import com.DTOS.MessageResposeDTO;
import com.entities.CustomUserDetails;
import com.entities.Message;
import com.entities.User;
import com.service.MessageService;
import com.service.UserService;

import jakarta.validation.Valid;

@Controller
public class GroupController {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired 
	private MessageService msgservice;
	@Autowired 
	private UserService service;

	public void sendToGroup(String group, MessageResposeDTO message) {
		String path = "/sock/studyApp/topic/"+ group;
		messagingTemplate.convertAndSend(path, message);
	}
	
	@MessageMapping("/group/chat")
	public void receiveMessage(@Valid @Payload MessageDTO msg, Principal authUser) {
		User sender = service.getByUsername(authUser.getName());
		sender.setStudySessions(sender.getStudySessions());
		System.out.println(msg);
		Message message = new Message(sender, msg);
		msgservice.createMessage(message);
		sendToGroup(msg.getSession().getGroupId(), new MessageResposeDTO(message));
	}
}
