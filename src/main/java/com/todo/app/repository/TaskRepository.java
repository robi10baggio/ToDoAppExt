package com.todo.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.todo.app.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
	public List<Task> findByStatusEquals(Integer status);
	
	public List<Task> findByStatusEqualsAndUserTeamIdOrderByDueDate(Integer status, long team_id);
	
	public List<Task> findByStatusLessThanAndUserTeamIdOrderByDueDate(Integer status, long team_id);

	public List<Task> findAllByOrderByDueDate();
	
}
