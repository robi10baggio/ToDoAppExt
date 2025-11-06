package com.todo.app.form;

import lombok.Data;

@Data
public class TodoSearchForm {
	private String keyword;
	private Long userId;
	private String userName;
	private Integer status;
	private String dueDateFrom;
	private String dueDateTo;
}
