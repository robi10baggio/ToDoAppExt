package com.todo.app.form;


import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class TaskForm {
	private Long id;
	
    @Size(min = 1, max = 200, message = "{0}は{1}文字以内{2}文字以上で入力してください。")
    private String taskContent;
	
    private Integer status;
	
	@NotEmpty
	private String dueDate;
	
	private Long userId;
	private String userName;

	private String teamName;
	
	private List<CommentForm> comments;
}
