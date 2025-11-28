package com.todo.app.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name="tasks")
@Data
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="task_content", nullable = false)
	private String taskContent;
	
	private Integer status;
	
	@Column(name="due_date", nullable = false)
	private LocalDate dueDate;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name="task_id")
	private List<Comment> comments;
}
