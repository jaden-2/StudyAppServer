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

import com.DTOS.MessageDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer messageId;
	
	@ManyToOne
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User sender;
	
	@Nonnull
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "sessionId", referencedColumnName = "sessionId")
	@JsonIgnore
	private StudySession session;
	
	private LocalDateTime createdAt;
	
	@PrePersist
	private void setCreated() {
		this.createdAt = LocalDateTime.now();
	}
	
	public Message(User sentFrom, String content, StudySession session) {
		this.content = content;
		this.sender = sentFrom;
		this.session = session;
	}
	public Message(User sender, MessageDTO message) {
		this.sender = sender;
		this.content = message.getContent();
		this.session= new StudySession(message.getSession());
	}
	
}
