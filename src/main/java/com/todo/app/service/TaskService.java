package com.todo.app.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.todo.app.entity.Task;
import com.todo.app.form.TaskSearchForm;
import com.todo.app.model.TaskSpecifications;
import com.todo.app.repository.TaskRepository;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	public List<Task> searchIncomplete(Long teamId,TaskSearchForm form) {
	    // 検索条件をもとにクエリを構築
	    List<Specification<Task>> specs = new ArrayList<>();

	    if (form.getKeyword() != null && !form.getKeyword().isEmpty()) {
	    	specs.add(TaskSpecifications.taskContains(form.getKeyword()));
	    }
	    if (form.getUserId() != null) {
	    	specs.add(TaskSpecifications.userIdIs(form.getUserId()));
	    }
	    specs.add(TaskSpecifications.teamIdIs(teamId));

	    specs.add(TaskSpecifications.statusLessThan(2));
	    if (form.getDueDateFrom() != null && !form.getDueDateFrom().isEmpty()) {
	    	Date fromDate = Date.valueOf(form.getDueDateFrom());
	    	specs.add(TaskSpecifications.dueDateAfter(fromDate));
	    }
	    if (form.getDueDateTo() != null && !form.getDueDateTo().isEmpty()) {
	    	Date toDate = Date.valueOf(form.getDueDateTo());
	    	specs.add(TaskSpecifications.dueDateBefore(toDate));
	    }
	    
	    Specification<Task> finalSpec = specs.stream()
	    	    .reduce(Specification::and)
	    	    .orElse(null);
	    Sort sort = Sort.by(Sort.Direction.ASC, "dueDate"); // 昇順ソート
	    
	    return taskRepository.findAll(finalSpec, sort);
	}
	
	public List<Task> searchComplete(Long teamId,TaskSearchForm form) {
		 // 検索条件をもとにクエリを構築
	    List<Specification<Task>> specs = new ArrayList<>();

	    if (form.getKeyword() != null && !form.getKeyword().isEmpty()) {
	    	specs.add(TaskSpecifications.taskContains(form.getKeyword()));
	    }
	    if (form.getUserId() != null) {
	    	specs.add(TaskSpecifications.userIdIs(form.getUserId()));
	    }
	    specs.add(TaskSpecifications.teamIdIs(teamId));

	    specs.add(TaskSpecifications.statusIs(2));

	    if (form.getDueDateFrom() != null && !form.getDueDateFrom().isEmpty()) {
	    	Date fromDate = Date.valueOf(form.getDueDateFrom());
	    	specs.add(TaskSpecifications.dueDateAfter(fromDate));
	    }
	    if (form.getDueDateTo() != null && !form.getDueDateTo().isEmpty()) {
	    	Date toDate = Date.valueOf(form.getDueDateTo());
	    	specs.add(TaskSpecifications.dueDateBefore(toDate));
	    }
	    
	    Specification<Task> finalSpec = specs.stream()
	    	    .reduce(Specification::and)
	    	    .orElse(null);
	    
	    Sort sort = Sort.by(Sort.Direction.ASC, "dueDate"); // 昇順ソート
	    return taskRepository.findAll(finalSpec, sort);
	}

	public List<Task> selectAll(){
		return taskRepository.findAllByOrderByDueDate();
	}

	public List<Task> selectIncomplete(long team_id) {
		return taskRepository.findByStatusLessThanAndUserTeamIdOrderByDueDate(2, team_id);
	}

	public List<Task> selectComplete(long team_id) {
		return taskRepository.findByStatusEqualsAndUserTeamIdOrderByDueDate(2, team_id);
	}

	public void add(Task todo) {
		taskRepository.save(todo);
	}

	@Transactional
	public void update(Task todo) {
		taskRepository.save(todo);
	}

	@Transactional
	public void delete(long id) {
		taskRepository.deleteById(id);
	}
}
