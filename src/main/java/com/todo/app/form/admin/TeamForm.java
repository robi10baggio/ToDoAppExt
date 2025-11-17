package com.todo.app.form.admin;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.todo.app.entity.User;

import lombok.Data;

@Data
public class TeamForm {
	private Long id;
	
	@NotEmpty
	@Size(min = 1, max = 20, message = "{0}は{1}文字以上{2}文字以下で入力してください。")
	private String teamName;
	
	private String description;

	private List<User> members;
}
