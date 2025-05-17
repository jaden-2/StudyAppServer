package com.DTOS;

import com.entities.CustomUserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

	private String username;
	
	
	public UserResponse(CustomUserDetails user) {
		this.username = user.getUsername();
		
	}
	
}
