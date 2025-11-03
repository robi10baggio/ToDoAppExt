package com.todo.app.form;

import lombok.Data;

@Data
public class TodoSearchForm {
	private String keyword;
	private String userName;
	private Long userId;
	private Integer status;
	private String timeLimitFrom;
	private String timeLimitTo;
}
