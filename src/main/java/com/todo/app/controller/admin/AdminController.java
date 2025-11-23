package com.todo.app.controller.admin;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.todo.app.entity.User;
import com.todo.app.form.LoginForm;
import com.todo.app.model.Account;
import com.todo.app.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private HttpSession session;
	
	@Autowired
	private Account account;
	
	@Autowired
	private UserService userService;

	// 管理者ログインを表示
	@GetMapping({ "/", "/login", "/logout" })
	public String showAdminPage(
			LoginForm loginForm,
			@RequestParam(name = "error", defaultValue = "") String error,
			Model model) {
			// 管理者画面の表示ロジックをここに実装
			// セッション情報を全てクリアする
			session.invalidate();
			if (error.equals("notLoggedIn")) {
				model.addAttribute("message", "ログインしてください");
			}
		return "login-admin";
	}

	// ログインを実行
	@PostMapping("/login")
	public String loginAdmin(
		@Validated LoginForm loginForm,
		BindingResult bindingResult,
		RedirectAttributes redirectAttribute,
		Model model) {
		if (bindingResult.hasErrors()) {
			return "login";
		}
		User user = userService.loginAuth(loginForm.getUserId(), loginForm.getPassword());
			
		if (user == null || !user.isAdmin()) {
			// エラーパラメータのチェック
			model.addAttribute("message", "ログインしてください");
			return "login-admin";
		}
		// セッション管理されたアカウント情報に名前をセット
		account.setUserId(user.getId());
		account.setRole(user.getRole());
		account.setUserName(user.getUserName());
		account.setTeamName(user.getTeam().getTeamName());

		// 「/todo」へのリダイレクト
		return "redirect:/admin/teams-dashboard";
	}
}
