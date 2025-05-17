package com.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entities.Message;
import com.exceptions.InvalidOperationException;
import com.repository.MessageRepo;

@Service
public class MessageService {
	@Autowired
	private MessageRepo repo;
	/*
	 * Gets message by messageId
	 * @Returns: Message
	 * @Throws: NoSuchElementException, message does not exist*/
	public Message getMessageById(Integer id) throws NoSuchElementException {
	
		return repo.findById(id).orElseThrow(()-> new NoSuchElementException("Message does not exist"));
	}
	
	/*
	 * Get all messages for a group
	 * @Returns: List<Message>
	 * @Throws: NoSuchElementException, group does not exist*/
	public List<Message> getMessagesByGroup(Integer session)throws NoSuchElementException{
		return repo.findBySessionSessionId(session).orElseThrow(()-> new NoSuchElementException("Group does not exist"));
	}
	
	/*
	 * Creates a new message
	 * @Returns: Message, created message
	 * */
	public Message createMessage(Message message) {
		repo.save(message);
		return message;
	}
	
	/*
	 * Edit already sent message
	 * @Returns: Message, updated message
	 * @Throws: NoSuchElementExcpetion, cannot update a message that has not been sent
	 * @Throws: InvalidOperationException, cannot modify a message belonging to a different group*/
	public Message updateMessage(Message updatedMessage) throws NoSuchElementException, InvalidOperationException{
		Message message = repo.findById(updatedMessage.getMessageId())
				.orElseThrow(()-> new NoSuchElementException("Cannot update message that does not exist"));
		message.setContent(updatedMessage.getContent());
		message.setCreatedAt(updatedMessage.getCreatedAt());
		
		if(message.getSession() != updatedMessage.getSession())
			throw new InvalidOperationException("Cannot perform operation");
		repo.save(message);
		
		return message;
	}
	
	/*
	 * Deletes an already sent message
	 * @Returns: void
	 * @Throws: NoSuchElementException, cannot delete a message that does not exist
	 * @Throws: InvalidOperationException, cannot delete a message that belongs to another group*/
	public void deleteMessage(Integer messageId, String groupId) throws NoSuchElementException, InvalidOperationException {
		Message msg = repo.findById(messageId).orElseThrow(()-> new NoSuchElementException("Message does not exist"));
		
		if(msg.getSession().getGroupId() != groupId)
			throw new InvalidOperationException("Cannot perform this operation");
		
		repo.delete(msg);
	}
	
	/*
	 * Deletes an already sent message
	 * @Returns: void
	 * @Throws: NoSuchElementException, cannot delete a message that does not exist
	 * @Throws: InvalidOperationException, cannot delete a message that belongs to another group*/
	public void deleteMessage(Message message) throws NoSuchElementException, InvalidOperationException{
		Message msg = repo.findById(message.getMessageId()).orElseThrow(()-> new NoSuchElementException("Message does not exist"));
		if(msg.getSession() != message.getSession())
			throw new InvalidOperationException("Cannot perform this operation");
		
		repo.delete(msg);
				 
	}
}
