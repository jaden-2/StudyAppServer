package com.DTO;

import java.util.List;

import com.entities.Message;
import com.entities.StudySession;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudySessionDTO {
	private Integer id;
	private List<Message> messages;
	
	
	@NotBlank(message = "Group must have title")
	@Size(max = 150, message="Title cannot be more than 150 characters")
	private String title;
	@Size(max = 250, message = "Description cannot be more than 250 characters")
	private String description;
	
	public StudySessionDTO(StudySession session) {
		this.title = session.getTitle();
		this.description = session.getDescription();
		this.id = session.getSessionId();
		this.messages = session.getMessages();
	}
}
