package com.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudySessionDTO {
	@NotBlank(message = "Group must have title")
	@Size(max = 150, message="Title cannot be more than 150 characters")
	private String title;
	@Size(max = 250, message = "Description cannot be more than 250 characters")
	private String description;
	
}
