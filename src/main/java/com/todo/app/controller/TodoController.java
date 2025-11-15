package com.todo.app.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.todo.app.entity.Comment;
import com.todo.app.entity.Task;
import com.todo.app.entity.User;
import com.todo.app.form.CommentForm;
import com.todo.app.form.TaskForm;
import com.todo.app.form.TaskSearchForm;
import com.todo.app.model.Account;
import com.todo.app.service.CommentService;
import com.todo.app.service.TaskService;
import com.todo.app.service.UserService;

@Controller
@RequestMapping("/todo")
public class TodoController {
	@Autowired
	private Account account;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	private static Map<Integer, String> statusMenumap = new HashMap<>();
    static {
    	statusMenumap.put(0, "未着手");
    	statusMenumap.put(1, "実施中");
    	statusMenumap.put(2, "完了");
    }
    
    @ModelAttribute("statusMenu")
    public static  Map<Integer, String> getStatusMenu() {
		return TodoController.statusMenumap;
    }
    
    @ModelAttribute("account")
    public Account getAccount() {
		return this.account;
    }
    
    @ModelAttribute("userList")
    public Map<Long,String> getUserList() {
		User user = userService.findById(account.getUserId());
		Long teamId = user.getTeam().getId();
		List<User> userList = userService.findByTeamId(teamId);
		
		// ユーザーIDとユーザー名のMapを作成
		Map<Long,String> userMap = new HashMap<>();
		for (User u:userList) {
			userMap.put(u.getId(),u.getUserName());
		}
		return userMap;
	}
    
    private void updateList(List<Task> list, List<Task> doneList, Model model) {
		List<TaskForm> forms = new ArrayList<>();
		for (Task task: list) {
			// Entityから表示用のFormに変換
			TaskForm form = new TaskForm();
			form.setId(task.getId());
			form.setTaskContent(task.getTaskContent());
			form.setStatus(task.getStatus());
			form.setUserId(task.getUser().getId());
			form.setUserName(task.getUser().getUserName());
			form.setTeamName(task.getUser().getTeam().getTeamName());
			
			form.setDueDate(task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			List<CommentForm> commForms = new ArrayList<>();
			List<Comment>comments = task.getComments();
			for (Comment comment:comments) {
			
				CommentForm commForm = new CommentForm();
				commForm.setComment(comment.getComment());
				commForm.setUserName(userService.findById(comment.getUserId()).getUserName());
				commForm.setPostDateTime(comment.getPostDateTime());
				commForms.add(commForm);
			}
			
			form.setComments(commForms);;
			forms.add(form);
		}
		model.addAttribute("todos",forms);
		
		List<TaskForm> doneForms = new ArrayList<>();
		for (Task task:doneList) {
			// Entityから表示用のFormに変換
			TaskForm form = new TaskForm();
			form.setId(task.getId());
			form.setTaskContent(task.getTaskContent());
			form.setStatus(task.getStatus());
			form.setUserId(task.getUser().getId());
			form.setUserName(task.getUser().getUserName());
			form.setTeamName(task.getUser().getTeam().getTeamName());
			
			form.setDueDate(task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			List<CommentForm> commForms = new ArrayList<>();
			List<Comment>comments = task.getComments();
			for (Comment comment:comments) {
			
				CommentForm commForm = new CommentForm();
				commForm.setComment(comment.getComment());
				commForm.setUserName(userService.findById(comment.getUserId()).getUserName());
				commForm.setPostDateTime(comment.getPostDateTime());
				commForms.add(commForm);
			}
			
			form.setComments(commForms);
			doneForms.add(form);
		}
		
		model.addAttribute("doneTodos",doneForms);
    }
    
	@GetMapping("/list")
	public String showListPage(
			TaskForm taskForm, 
			TaskSearchForm todoSearchForm,
			Model model) {
		User user = userService.findById(account.getUserId());
    	Long teamId = user.getTeam().getId();
		List<Task> list = taskService.selectIncomplete(teamId);
		List<Task> doneList = taskService.selectComplete(teamId);
		updateList(list, doneList, model);
		
		return "Todo-list";
	}
	
	@GetMapping("/add")
	public String showAddTaskForm(TaskForm taskForm, Model model) {
		return "Todo-add";
	}
	
	@PostMapping("/add")
	public String addTask(
			@Validated TaskForm taskForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttribute,
			Model model) {

		//バリデーションチェック
		if (bindingResult.hasErrors()) {
			return "Todo-add";
		}
		Task task = new Task();
		task.setTaskContent(taskForm.getTaskContent());
		task.setDueDate(LocalDate.parse(taskForm.getDueDate()));
		task.setStatus(0);
		User user = userService.findById(account.getUserId()); 

		task.setUser(user);
		taskService.add(task);
		
		return "redirect:/todo/list";
	}

	@PostMapping("/update/{id}")
	public String updateTask(
			@PathVariable Long id, 
			@Validated TaskForm taskForm) {

		Task task = new Task();
		task.setId(id);
		task.setTaskContent(taskForm.getTaskContent());
		task.setDueDate(LocalDate.parse(taskForm.getDueDate()));
		
		task.setStatus(taskForm.getStatus());
		User user = userService.findById(account.getUserId()); 

		task.setUser(user);
		taskService.update(task);
		return "redirect:/todo/list";
	}
	
	@PostMapping("/delete/{id}")
	public String deleteTask(@PathVariable Long id) {
		taskService.delete(id);
		return "redirect:/todo/list";
	}
	
	
	// 検索実行
	@PostMapping("/search")
	public String searchTasks(
			TaskForm taskForm, 
			@Validated TaskSearchForm taskSearchForm, 
			Model model) {
		
		User user = userService.findById(account.getUserId());
    	Long teamId = user.getTeam().getId();
		List<Task> list = taskService.searchIncomplete(teamId, taskSearchForm);
		List<Task> doneList = taskService.searchComplete(teamId, taskSearchForm);
		updateList(list, doneList, model);
	    
		return "Todo-list";
	}
	
	// コメント追加処理
	@PostMapping("/comment-add/{id}")
	public String Addcomment(
			@PathVariable Long id, 
			@RequestParam String comment, Model model) {
		
		Comment commentObj = new Comment();
		commentObj.setComment(comment);
		commentObj.setTaskId(id);
		commentObj.setUserId(account.getUserId());
		commentObj.setPostDateTime(LocalDateTime.now());
		
		commentService.add(commentObj);
		return "redirect:/todo/list";
	}
}
