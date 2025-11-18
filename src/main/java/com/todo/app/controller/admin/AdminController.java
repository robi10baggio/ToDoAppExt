package com.todo.app.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.todo.app.entity.User;
import com.todo.app.form.LoginForm;
import com.todo.app.model.Account;
import com.todo.app.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private Account admin;
	
	@Autowired
	private UserService userService;

	// 管理者ログインを表示
	@GetMapping({ "/", "/login", "/logout" })
	public String showAdminPage(
			LoginForm loginForm,
			Model model) {
		// 管理者画面の表示ロジックをここに実装
		return "/admin/login-admin";
	}

	// ログインを実行
	@PostMapping("/login")
	public String loginAdmin(
		@Validated LoginForm loginForm,
		BindingResult bindingResult,
		RedirectAttributes redirectAttribute,
		Model model) {
		if (bindingResult.hasErrors()) {
			return "/admin/login";
		}
		User user = userService.loginAuth(loginForm.getUserId(), loginForm.getPassword());
			
		if (user == null || !user.isAdmin()) {
			// エラーパラメータのチェック
			model.addAttribute("message", "ログインしてください");
			return "admin/login";
		}
		// セッション管理されたアカウント情報に名前をセット
		admin.setUserId(user.getId());
		admin.setUserName(user.getUserName());
		admin.setTeamName(user.getTeam().getTeamName());

		// 「/todo」へのリダイレクト
		return "redirect:/admin/teams-dashboard";
	}
}
