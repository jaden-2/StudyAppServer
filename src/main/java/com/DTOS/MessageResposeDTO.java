package com.DTOS;

import java.time.LocalDateTime;

import com.entities.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResposeDTO {
	private String content;
	private String sender;
	private LocalDateTime createdAt;
	
	public MessageResposeDTO(Message msg) {
		this.content = msg.getContent();
		this.sender = msg.getSender().getUsername();
		this.createdAt = msg.getCreatedAt();
	}
}
