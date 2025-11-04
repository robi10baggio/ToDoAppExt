package com.todo.app.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name="comments")
@Data
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="todo_id")
	private Long todoId;
	
	private String	comment;
	
	@Column(name="post_date")
	private Date postDate;	
	
	@Column(name="user_id")
	private	   Long userId;
}
