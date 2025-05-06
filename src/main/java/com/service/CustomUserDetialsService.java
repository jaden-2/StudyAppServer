package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.entities.CustomUserDetails;
import com.entities.User;
import com.repository.UserRepo;

@Service
public class CustomUserDetialsService implements UserDetailsService{
	@Autowired
	private UserRepo repo;
	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
		
		return new CustomUserDetails(user);
	}

}
