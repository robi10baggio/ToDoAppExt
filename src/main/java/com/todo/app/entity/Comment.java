package com.todo.app.entity;

import java.time.LocalDateTime;

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
	
	@Column(name="task_id")
	private Long taskId;
	
	private String	comment;
	
	@Column(name="post_date_time")
	private LocalDateTime postDateTime;	
	
	@Column(name="user_id")
	private	   Long userId;
}
