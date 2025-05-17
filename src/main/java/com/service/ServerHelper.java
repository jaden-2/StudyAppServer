package com.service;


import com.DTOS.MessageDTO;
import com.DTOS.MessageResposeDTO;
import com.DTOS.StudySessionDTO;
import com.entities.Message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerHelper {
	private final MessageService msgService;
	private final UserService service;
	
	 public MessageResposeDTO createServerMessage(String username, String content, StudySessionDTO sess) {
			MessageDTO msg = new MessageDTO(username +" "+content,  sess);
			return new MessageResposeDTO(msgService.createMessage(new Message(service.getByUsername("Study App"), msg)));
		}
}
