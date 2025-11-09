package com.todo.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.app.entity.Comment;
import com.todo.app.repository.CommentRepository;

@Service
public class CommentService {
	@Autowired 
	private CommentRepository commentRepository;
	
	public void add(Comment comment) {
		commentRepository.save(comment);
	}
}
