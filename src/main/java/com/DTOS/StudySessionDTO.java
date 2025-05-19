package com.DTOS;
import java.util.ArrayList;
import java.util.List;

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
	private String groupId;
	private List<MessageResposeDTO> messages = new ArrayList<>();
	
	
	@NotBlank(message = "Group must have title")
	@Size(max = 150, message="Title cannot be more than 150 characters")
	private String title;
	@Size(max = 250, message = "Description cannot be more than 250 characters")
	private String description;
	
	public StudySessionDTO(StudySession session) {
		this.id = session.getSessionId();
		this.groupId = session.getGroupId();
		this.title = session.getTitle();
		this.description = session.getDescription();
		if(session.getMessages() !=null) {
			session.getMessages().forEach((msg)->messages.add(new MessageResposeDTO(msg)));
		}
		
	}
}
