package com.todo.app.form;

import java.util.Date;

import lombok.Data;

@Data
public class CommentForm {
	private Long id;
	private String comment;
	private String userName;
	private Date postDate;
}
