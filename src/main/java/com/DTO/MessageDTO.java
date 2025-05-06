package com.DTO;

import com.entities.StudySession;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
	@NotBlank(message="Can't send an empty message")
	private String content;
	@Nonnull
	private StudySession group;
}
