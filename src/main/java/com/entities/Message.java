package com.entities;


import java.time.LocalDateTime;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.DTO.MessageDTO;

@Entity
@Data
@NoArgsConstructor
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer messageId;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User sender;
	
	@Nonnull
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "sessionId")
	private StudySession group;
	
	private LocalDateTime createdAt;
	
	@PrePersist
	private void setCreated() {
		this.createdAt = LocalDateTime.now();
	}
	
	public Message(User sentFrom, String content, StudySession group) {
		this.content = content;
		this.sender = sentFrom;
		this.group = group;
	}
	public Message(User sender, MessageDTO message) {
		this.sender = sender;
		this.content = message.getContent();
		this.group = message.getGroup();
	}
	
}
