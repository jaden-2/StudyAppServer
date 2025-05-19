package com.service;

import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.DTOS.StudySessionDTO;
import com.DTOS.UserRequest;
import com.DTOS.UserResponse;
import com.entities.CustomUserDetails;
import com.entities.StudySession;
import com.entities.User;
import com.repository.UserRepo;

@Service
public class UserService {
	
	private final UserRepo repo;
	private final PasswordEncoder encoder;
	
	public UserService(UserRepo userRepo, PasswordEncoder encoder) {
		this.repo = userRepo;
		this.encoder = encoder;
	}
	
	
	public void createUser(UserRequest newUser) {
		User user = new User(newUser);
		user.setPassword(encoder.encode(user.getPassword()));
		user.setPassChangedAt(new Date());
		repo.save(user);
	}
	
	public UserResponse getUser(CustomUserDetails authenticatedUser) {
		return new UserResponse(authenticatedUser);
	}
	
	public User getUser(String username) {
		return repo.findByUsername(username).orElseThrow();
	}
	
	/*
	 * Updates the credentials of an authenticated user. After an update, a user should 
	 * re-authenticate
	 * @Parameters: Authenticated user from security context 
	 * @Throws: UsernameNotFoundException, user does not exist (BadCredentialsException)*/
	public void updateUser(UserRequest updatedUser) throws UsernameNotFoundException {
		User user = repo.findByUsername(updatedUser.getUsername())
				.orElseThrow(()-> new UsernameNotFoundException("User does not exist"));
		
		user.setUsername(updatedUser.getUsername());
		user.setPassword(encoder.encode(updatedUser.getPassword()));
		user.setPassChangedAt(new Date());
		repo.save(user);
		
	}
	
	public void deleteUser(CustomUserDetails authenticatedUser) {
		User user = repo.findByUsername(authenticatedUser.getUsername())
				.orElseThrow(()-> new UsernameNotFoundException("Cannot delete user"));
		
		repo.delete(user);
	}
	/*
	 * Users joins existing StudySessions
	 * */
	public StudySessionDTO joinGroup(CustomUserDetails authenticatedUser, StudySession group) {
		User user = repo.findByUsername(authenticatedUser.getUsername()).orElseThrow();
		user.getStudySessions().add(group);
		
		repo.save(user);
		return new StudySessionDTO(group);
	}
	
	/*Leave an existing group*/
	public void leaveGroup(CustomUserDetails authenUser, StudySession group) {
		User user = repo.findByUsername(authenUser.getUsername()).orElseThrow();
		user.getStudySessions().remove(group);
		repo.save(user);
	}
	
	/*
	 * Gets the groups an authenticated user belongs to 
	 * @Param: UserRequest, the authenticated user principal context from spring security
	 * @Returns: Set<StudySession>, a set of all groups the users belongs to*/
	public Set<StudySessionDTO> getGroups(CustomUserDetails user) {
		//unauthenticated access should not be possible
		Set<StudySession> sessions = repo.findByUsername(user.getUsername())
				.orElseThrow(()-> new NoSuchElementException("User does not exist")).getStudySessions();
		
		Set <StudySessionDTO> sessionDTOs = new HashSet<>();
		
		sessions.forEach((session)-> sessionDTOs.add(new StudySessionDTO(session)));
		return sessionDTOs;
	}


	public User getByUsername(String name) {
		return repo.findByUsername(name).orElseThrow();
	}
	
	
}
