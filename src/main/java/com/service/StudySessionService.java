package com.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entities.StudySession;
import com.repository.StudySessionRepo;


@Service
public class StudySessionService {
	@Autowired
	private StudySessionRepo repo;
	
	public void createGroup(StudySession group) {
		repo.save(group);
	}
	
	public StudySession updateGroup(StudySession updatedGroup) throws NoSuchElementException{
		StudySession group = repo.findById(updatedGroup.getSessionId()).orElseThrow();
		group.setDescription(updatedGroup.getDescription());
		group.setTitle(updatedGroup.getTitle());
		
		repo.save(group);
		
		return group;
	}
	
	/*For future, study group should be able to manage/track users as they join and leave the group
	 * A group should be deleted when there is no user in it
	 * The StudySession should have a @manytomany annotation so it can keep track of users are they join the group*/
	public void deleteGroup(StudySession group) {
		StudySession session = repo.findById(group.getSessionId()).orElseThrow();
		repo.delete(session);
	}
	public StudySession getSession(Integer sessionId) {
		return repo.findById(sessionId).orElseThrow(()-> new NoSuchElementException("Session does not exist"));
	}
}
