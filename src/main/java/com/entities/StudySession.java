package com.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.DTOS.StudySessionDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StudySession {
	
	public StudySession(StudySessionDTO group) {
		this.sessionId = group.getId();
		this.title = group.getTitle();
		this.Description = group.getDescription();
		this.groupId = group.getGroupId();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer sessionId;
	
	
	@NotBlank
	@Size(max = 150)
	private String title;
	
	@Size(max = 250)
	private String Description;
	
	@Column(nullable = false, unique = true)
	@NotBlank
	private String groupId;
	
	@OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "session")
	@JsonIgnore
	private List<Message> messages;
	
	@ManyToMany(mappedBy= "studySessions")
	private Set<User> users = new HashSet<>();
	@PrePersist
	private void setGroup() {
		this.groupId = "group-" + UUID.randomUUID().toString().substring(0, 8);
	}
	
}
