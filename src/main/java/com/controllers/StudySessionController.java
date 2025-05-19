package com.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DTOS.MessageResposeDTO;
import com.DTOS.StudySessionDTO;
import com.entities.CustomUserDetails;
import com.entities.StudySession;
import com.service.StudySessionService;
import com.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/studyApp/group")
public class StudySessionController {
	
	@Autowired
	private StudySessionService service;
	
	@Autowired
	private SimpMessagingTemplate msgTemplate;
	
	@Autowired 
	private UserService userService;
	
	private static final String brokerPath= "/sock/studyApp/topic/";
	
	@GetMapping("/{id}")
	public ResponseEntity<StudySessionDTO> getGroup(@PathVariable Integer id){
		return ResponseEntity.ok(new StudySessionDTO(service.getSession(id)));
	}
	
	/*
	 * Creates a group for authenticated user, adds this group to user groups, announce that user created group*/
	@PostMapping
	public ResponseEntity<StudySessionDTO> createGroup(@AuthenticationPrincipal CustomUserDetails authUser, @Valid @RequestBody StudySessionDTO group){
		
		StudySession newGroup = new StudySession(group); // group is instantiated
		
		service.createGroup(newGroup);// group is created
		userService.joinGroup(authUser, newGroup);// group is added to user's list of groups
		MessageResposeDTO message = UserController.createServerMessage(authUser.getUsername(), "created group"); // prepares announcement
		
		msgTemplate.convertAndSend(brokerPath+ newGroup.getGroupId(), message); // makes announcement
		
		return ResponseEntity.created(URI.create("/studyApp/group/"+newGroup.getGroupId())).body(new StudySessionDTO(newGroup));
	}
	/*
	 * Updates the title and description of a group*/
	@PutMapping
	public ResponseEntity<Void> updateGroup(@AuthenticationPrincipal CustomUserDetails authUser, @Valid @RequestBody StudySession group){
		StudySession session = service.getSession(group.getSessionId());
		session.setDescription(group.getDescription());
		session.setTitle(group.getTitle());
		service.updateGroup(session);
		return ResponseEntity.accepted().build();
	}
	
}
