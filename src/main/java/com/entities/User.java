package com.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.DTOS.UserRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "study_user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer userId;
	
	@Nonnull
	@Column(unique = true, nullable = false)
	private String username;
	
	@Nonnull
	private String password;
	
	@JsonIgnore
	private Date passChangedAt;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_studySessions", joinColumns = @JoinColumn(name = "user_id"), 
	inverseJoinColumns = @JoinColumn(name="session_id"))
	private Set<StudySession> studySessions = new HashSet<>();
	
	public User(UserRequest newUser) {
		this.password = newUser.getPassword();
		this.username = newUser.getUsername();
	}
	
}
