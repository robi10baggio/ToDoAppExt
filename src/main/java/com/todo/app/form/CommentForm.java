package com.todo.app.form;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CommentForm {
	private Long id;
	private String comment;
	private String userName;
	private LocalDate postDate;
}
