package com.todo.app.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.todo.app.entity.Team;
import com.todo.app.entity.User;
import com.todo.app.form.admin.UserForm;
import com.todo.app.service.TeamService;
import com.todo.app.service.UserService;

@Controller
@RequestMapping("/admin")
public class UserManagementController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;
	
	
	private static Map<Integer, String> roleMap = new HashMap<>();
    static {
    	roleMap.put(0, "ユーザ");
    	roleMap.put(1, "管理者");
    }
    
	@ModelAttribute("roleMap")
	public Map<Integer, String> getRoleMap() {
		return UserManagementController.roleMap;
	}

	@ModelAttribute("teamMenu")
    public Map<Integer, String> getTeamsMenu() {
		Map<Integer, String> teamMap = new HashMap<>();
		List<Team> teams = teamService.findAll();
		for (Team team:teams) {
			teamMap.put((int) team.getId(), team.getTeamName());
		}
		return teamMap;
    }
	
	@GetMapping("users-dashboard")
	public String showDashboard(
			UserForm userForm,
			Model model) {
		// ダッシュボードの表示ロジックをここに実装
		List<User> memberList = userService.findAllByOrderById();
		List<UserForm> tempUserFormList = new ArrayList<>();
		
		for (User user : memberList) {
			UserForm form = new UserForm();
			form.setId(user.getId());
			form.setUserId(user.getUserId());
			form.setUserName(user.getUserName());
			form.setRole(user.getRole());
			form.setTeamId(user.getTeam().getId());
			tempUserFormList.add(form);
		}
		model.addAttribute("userList", tempUserFormList);
		
		List<Team> teamList = teamService.findAllByOrderById();
		model.addAttribute("teamList", teamList);
		
		return "users-dashboard";
		
	}

	@GetMapping("/add-user")
	public String showRegistForm(
			UserForm userForm,
			Model model) {
		return "add-user";
	}
	
	@PostMapping("/add-user")
	public String registUser(
			@Validated UserForm userForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttribute,
			Model model) {
		if (bindingResult.hasErrors()) {
			return "/add-user";
		}
		if (!userForm.getPassword().equals(userForm.getCheckPassword())) {
			model.addAttribute("message", "パスワードが一致しません。");
			return "add-user";
		}
		User user = new User();
		user.setUserId(userForm.getUserId());
		user.setUserName(userForm.getUserName());
		user.setPassword(userForm.getPassword());
		Team team = teamService.findById((long)userForm.getTeamId());
		user.setTeam(team);
		try {
			userService.regist(user);
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("message", "既にユーザIDは登録されています。");
			return "/add-user";
		}
		return "redirect:/admin/users-dashboard";
		
	}
	
	@PostMapping("/update-user/{id}")
	public String updateUser(
		@PathVariable Long id, 
		@Validated UserForm userForm,
		BindingResult bindingResult,
		RedirectAttributes redirectAttribute,
		Model model) {
		//バリデーションチェック
		if (bindingResult.hasErrors()) {
			return "users-dashboard";
		}		
		
		User user = userService.findById(id);
		user.setUserId(userForm.getUserId());
		user.setUserName(userForm.getUserName());
		user.setRole(userForm.getRole());
		user.setTeam(teamService.findById(userForm.getTeamId()));

		userService.update(user);
		return "redirect:/admin/users-dashboard";
	}
	
	@PostMapping("/delete-user/{id}")
	public String deleteUser(
			@PathVariable Long id,
			RedirectAttributes redirectAttribute,
			Model model) {
		try {
			userService.delete(id);
		} catch (DataIntegrityViolationException e) {
			redirectAttribute.addFlashAttribute("deleteError", "このユーザは削除できません。関連するタスクが存在します。");
			return "redirect:/admin/users/users-dashboard";
		}
		return "redirect:/admin/users-dashboard";
	}
}
