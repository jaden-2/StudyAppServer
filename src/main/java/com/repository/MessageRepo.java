package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entities.Message;

@Repository
public interface MessageRepo extends JpaRepository<Message, Integer>{

	Optional<List<Message>> findByGroupGroupId(String groupId);

}
