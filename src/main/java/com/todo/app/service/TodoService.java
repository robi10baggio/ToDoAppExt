package com.todo.app.service;

import java.sql.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.todo.app.entity.Todo;
import com.todo.app.form.TodoSearchForm;
import com.todo.app.model.TodoSpecifications;
import com.todo.app.repository.TodoRepository;

@Service
public class TodoService {
	
	@Autowired
	TodoRepository todoRepository;
	
	public List<Todo> searchIncomplete(Long teamId,TodoSearchForm form) {
	    // 検索条件をもとにクエリを構築
	    Specification<Todo> spec = Specification.where(null);

	    if (form.getKeyword() != null && !form.getKeyword().isEmpty()) {
	    	spec = spec.and(TodoSpecifications.taskContains(form.getKeyword()));
	    }
	    if (form.getUserId() != null) {
	    	spec = spec.and(TodoSpecifications.userIdIs(form.getUserId()));
	    }
	    spec = spec.and(TodoSpecifications.teamIdIs(teamId));

	    spec = spec.and(TodoSpecifications.statusLessThan(2));
	    if (form.getTimeLimitFrom() != null && !form.getTimeLimitFrom().isEmpty()) {
	    	Date fromDate = Date.valueOf(form.getTimeLimitFrom());
	    	spec = spec.and(TodoSpecifications.dueDateAfter(fromDate));
	    }
	    if (form.getTimeLimitTo() != null && !form.getTimeLimitTo().isEmpty()) {
	    	Date toDate = Date.valueOf(form.getTimeLimitFrom());
	    	spec = spec.and(TodoSpecifications.dueDateBefore(toDate));
	    }

	    return todoRepository.findAll(spec);
	}
	
	public List<Todo> searchComplete(Long teamId,TodoSearchForm form) {
	    // 検索条件をもとにクエリを構築
	    Specification<Todo> spec = Specification.where(null);

	    if (form.getKeyword() != null && !form.getKeyword().isEmpty()) {
	    	spec = spec.and(TodoSpecifications.taskContains(form.getKeyword()));
	    }
	    if (form.getUserId() != null) {
	    	spec = spec.and(TodoSpecifications.userIdIs(form.getUserId()));
	    }

	    spec = spec.and(TodoSpecifications.statusIs(2));

	    if (form.getTimeLimitFrom() != null  && !form.getTimeLimitFrom().isEmpty()) {
	    	Date fromDate = Date.valueOf(form.getTimeLimitFrom());
	    	spec = spec.and(TodoSpecifications.dueDateAfter(fromDate));
	    }
	    if (form.getTimeLimitTo() != null  && !form.getTimeLimitFrom().isEmpty()) {
	    	Date toDate = Date.valueOf(form.getTimeLimitFrom());
	    	spec = spec.and(TodoSpecifications.dueDateBefore(toDate));
	    }

	    return todoRepository.findAll(spec);
	}

	public List<Todo> selectAll(){
		return todoRepository.findAll();
	}

	public List<Todo> selectIncomplete(long team_id) {
		return todoRepository.findByStatusLessThanAndTeamId(2, team_id);
	}

	public List<Todo> selectComplete(long team_id) {
		return todoRepository.findByStatusEqualsAndTeamId(2, team_id);
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
