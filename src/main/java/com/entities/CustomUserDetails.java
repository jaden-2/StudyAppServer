package com.entities;


import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class CustomUserDetails implements UserDetails{
	
	private String username;
	private User user;
	private String password;
	private Date passChangeDate;
	private Set<StudySession> groups;
	
	
	public CustomUserDetails(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.groups = user.getStudySessions();
		this.passChangeDate = user.getPassChangedAt();
		this.user = user;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	
	public Set<StudySession> getGroups() {
		return groups;
	}
	public User getUser() {
		return user;
	}
	public Date getPassChangeDate() {
		return passChangeDate;
	}
}
