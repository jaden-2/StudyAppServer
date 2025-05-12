package com.controllers;
import java.net.URI;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DTO.ServerResponse;
import com.DTO.StudySessionDTO;
import com.DTO.UserRequest;
import com.DTO.UserResponse;
import com.entities.CustomUserDetails;
import com.entities.StudySession;
import com.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("api/studyApp/account")
public class UserController {
	@Autowired
	private UserService service;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;// used to send message to group when user joins/leaves
	
	@GetMapping
	public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUserDetails authnUser){
		return ResponseEntity.ok(service.getUser(authnUser));
	}
	
	@PostMapping
	public ResponseEntity<ServerResponse> createAccount(@Valid @RequestBody UserRequest newUser){
		service.createUser(newUser);
		return ResponseEntity.created(URI.create("/login")).body(new ServerResponse("User has been create"));
	}
	
	@PutMapping
	public ResponseEntity<ServerResponse> updateUser(@AuthenticationPrincipal CustomUserDetails authUser, UserRequest updatedUser){
		try {
			service.updateUser(updatedUser);
		}catch(UsernameNotFoundException e) {
			return ResponseEntity.badRequest().body(new ServerResponse(e.getMessage()));
		}
		return ResponseEntity.accepted().body(new ServerResponse("Re-login to authenticate user"));
	}
	@DeleteMapping
	public ResponseEntity<ServerResponse> deleteUser(@AuthenticationPrincipal CustomUserDetails authUser){
		try {
			service.deleteUser(authUser);
		}catch (UsernameNotFoundException e) {
			ResponseEntity.badRequest().body(new ServerResponse(e.getMessage()));
		}
		return ResponseEntity.accepted().body(new ServerResponse("Account deleted"));
	}
	@PutMapping(path = "/group")
	public ResponseEntity<StudySession> joinGroup(@Valid @RequestBody StudySession group, @AuthenticationPrincipal CustomUserDetails authUser){
		ServerResponse message = new ServerResponse(authUser.getUsername() + " joined group");
		messagingTemplate.convertAndSend("/topic/group/"+group.getGroupId(), message);
		
		return ResponseEntity.accepted().body(service.joinGroup(authUser, group));
	}
	
	@DeleteMapping(path = "/group")
	public ResponseEntity<Void> leaveGroup(@AuthenticationPrincipal CustomUserDetails authUser, @Valid @RequestBody StudySession group){
		service.leaveGroup(authUser, group);
		ServerResponse message = new ServerResponse(authUser.getUsername() + " left group");
		messagingTemplate.convertAndSend("/topic/group/"+group.getGroupId(), message);
		
		return ResponseEntity.noContent().build();
	}
	@GetMapping(path= "/group")
	public ResponseEntity<Set<StudySessionDTO>> getUserStudyGroups(@AuthenticationPrincipal CustomUserDetails authUser){
		return ResponseEntity.ok(service.getGroups(authUser));
	}
	
}
