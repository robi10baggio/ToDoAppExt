package com.todo.app.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.todo.app.entity.Team;
import com.todo.app.entity.User;
import com.todo.app.form.admin.TeamForm;
import com.todo.app.service.TeamService;
import com.todo.app.service.UserService;

@Controller
@RequestMapping("/admin")
public class TeamManagementController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;

	@GetMapping("/teams-dashboard")
	public String showDashboard(
			TeamForm teamForm,
			Model model) {
		List<Team> teamList = teamService.findAll();
		List<TeamForm> tempTeamFormList = new ArrayList<>();
		
		for (Team team : teamList) {
			TeamForm form = new TeamForm();
			form.setId(team.getId());
			form.setTeamName(team.getTeamName());
			form.setDescription(team.getDescription());
			tempTeamFormList.add(form);
		}
		
		for (TeamForm form : tempTeamFormList) {
			form.setMembers(userService.findByTeamId(form.getId()));
		}
		
		model.addAttribute("teamList", tempTeamFormList);
		// ダッシュボードの表示ロジックをここに実装
		return "/admin/teams-dashboard";
		
	}
	
	@GetMapping("/add-team")
	public String showAddTeamForm(
			TeamForm teamForm,
			Model model) {
		
		List<User> allUsers = userService.findAll();
		model.addAttribute("allUsers", allUsers);
		// ダッシュボードの表示ロジックをここに実装
		return "/admin/add-team";
		
	}
	
	@PostMapping("/add-team")
	public String addTeam(
			@Validated TeamForm teamForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttribute,
			Model model) {

		//バリデーションチェック
		if (bindingResult.hasErrors()) {
			return "/admin/add-team";
		}
		Team team = new Team();
		team.setTeamName(teamForm.getTeamName());
		team.setDescription(teamForm.getDescription());

		teamService.regist(team);
		
		// ダッシュボードの表示ロジックをここに実装
		return "redirect:/admin/teams-dashboard";
		
	}
	
	@PostMapping("/update-team/{id}")
	public String updateTeam(
		@PathVariable Long id, 
		@Validated TeamForm teamForm,
		BindingResult bindingResult,
		RedirectAttributes redirectAttribute,
		Model model) {
		//バリデーションチェック
		if (bindingResult.hasErrors()) {
			return "/admin/teams-dashboard";
		}		
		
		Team team = new Team();
		team.setId(id);
		team.setTeamName(teamForm.getTeamName());
		team.setDescription(teamForm.getDescription());
	
		teamService.update(team);
		return "redirect:/adminteams-dashboard";
	}
	
	@PostMapping("/delete-team/{id}")
	public String deleteTeam(
			@PathVariable Long id,
			RedirectAttributes redirectAttribute,
			Model model) {
		try {
			teamService.delete(id);
		} catch (DataIntegrityViolationException e) {
			redirectAttribute.addFlashAttribute("deleteError", "このチームは削除できません。関連するユーザーが存在します。");
		}
		return "redirect:/admin/teams-dashboard";
	}
}
