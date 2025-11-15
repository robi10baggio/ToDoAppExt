package com.todo.app.form;

import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class TaskSearchForm {
	@Size(max = 200, message = "{0}は{1}文字以下で入力してください。")
	private String keyword;
	private Long userId;
	private Integer status;
	private String dueDateFrom;
	private String dueDateTo;
}
