package com.DTO;

import java.util.Set;

import com.entities.CustomUserDetails;
import com.entities.StudySession;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

	private String username;
	private Set<StudySession> studyGroups;
	
	public UserResponse(CustomUserDetails user) {
		this.username = user.getUsername();
		this.studyGroups = user.getGroups();
	}
	
}
