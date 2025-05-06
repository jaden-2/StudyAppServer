package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entities.StudySession;

@Repository
public interface StudySessionRepo extends JpaRepository<StudySession, Integer> {

}
