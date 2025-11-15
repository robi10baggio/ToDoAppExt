package com.todo.app.form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CommentForm {
	private Long id;
	@Size(max = 200, message = "{0}は{1}文字以下で入力してください。")
	private String comment;
	private String userName;
	private LocalDateTime postDateTime;
}
