package com.todo.app.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.todo.app.entity.Todo;
import com.todo.app.form.TodoSearchForm;
import com.todo.app.model.TodoSpecifications;
import com.todo.app.repository.TodoRepository;

@Service
public class TodoService {
	
	@Autowired
	private TodoRepository todoRepository;
	
	public List<Todo> searchIncomplete(Long teamId,TodoSearchForm form) {
	    // 検索条件をもとにクエリを構築
	    List<Specification<Todo>> specs = new ArrayList<>();

	    if (form.getKeyword() != null && !form.getKeyword().isEmpty()) {
	    	specs.add(TodoSpecifications.taskContains(form.getKeyword()));
	    }
	    if (form.getUserId() != null) {
	    	specs.add(TodoSpecifications.userIdIs(form.getUserId()));
	    }
	    specs.add(TodoSpecifications.teamIdIs(teamId));

	    specs.add(TodoSpecifications.statusLessThan(2));
	    if (form.getDueDateFrom() != null && !form.getDueDateFrom().isEmpty()) {
	    	Date fromDate = Date.valueOf(form.getDueDateFrom());
	    	specs.add(TodoSpecifications.dueDateAfter(fromDate));
	    }
	    if (form.getDueDateTo() != null && !form.getDueDateTo().isEmpty()) {
	    	Date toDate = Date.valueOf(form.getDueDateTo());
	    	specs.add(TodoSpecifications.dueDateBefore(toDate));
	    }
	    
	    Specification<Todo> finalSpec = specs.stream()
	    	    .reduce(Specification::and)
	    	    .orElse(null);
	    Sort sort = Sort.by(Sort.Direction.ASC, "dueDate"); // 昇順ソート
	    
	    return todoRepository.findAll(finalSpec, sort);
	}
	
	public List<Todo> searchComplete(Long teamId,TodoSearchForm form) {
		 // 検索条件をもとにクエリを構築
	    List<Specification<Todo>> specs = new ArrayList<>();

	    if (form.getKeyword() != null && !form.getKeyword().isEmpty()) {
	    	specs.add(TodoSpecifications.taskContains(form.getKeyword()));
	    }
	    if (form.getUserId() != null) {
	    	specs.add(TodoSpecifications.userIdIs(form.getUserId()));
	    }
	    specs.add(TodoSpecifications.teamIdIs(teamId));

	    specs.add(TodoSpecifications.statusIs(2));

	    if (form.getDueDateFrom() != null && !form.getDueDateFrom().isEmpty()) {
	    	Date fromDate = Date.valueOf(form.getDueDateFrom());
	    	specs.add(TodoSpecifications.dueDateAfter(fromDate));
	    }
	    if (form.getDueDateTo() != null && !form.getDueDateTo().isEmpty()) {
	    	Date toDate = Date.valueOf(form.getDueDateTo());
	    	specs.add(TodoSpecifications.dueDateBefore(toDate));
	    }
	    
	    Specification<Todo> finalSpec = specs.stream()
	    	    .reduce(Specification::and)
	    	    .orElse(null);
	    
	    Sort sort = Sort.by(Sort.Direction.ASC, "dueDate"); // 昇順ソート
	    return todoRepository.findAll(finalSpec, sort);
	}

	public List<Todo> selectAll(){
		return todoRepository.findAllByOrderByDueDate();
	}

	public List<Todo> selectIncomplete(long team_id) {
		return todoRepository.findByStatusLessThanAndUserTeamIdOrderByDueDate(2, team_id);
	}

	public List<Todo> selectComplete(long team_id) {
		return todoRepository.findByStatusEqualsAndUserTeamIdOrderByDueDate(2, team_id);
	}

	public void add(Todo todo) {
		todoRepository.save(todo);
	}

	@Transactional
	public void update(Todo todo) {
		todoRepository.save(todo);
	}

	@Transactional
	public void delete(long id) {
		todoRepository.deleteById(id);
	}
}
