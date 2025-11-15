package com.todo.app.model;

import java.sql.Date;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.todo.app.entity.Task;
import com.todo.app.entity.Team;
import com.todo.app.entity.User;

public class TaskSpecifications {
	public static Specification<Task> taskContains(String keyword) {
		return (root, query, cb) -> cb.like(root.get("taskContent"), "%" + keyword + "%");
	}

	public static Specification<Task> userIdIs(Long userId) {
		return (root, query, cb) -> {
			Join<Task, User> userJoin = root.join("user", JoinType.INNER);
            
			return cb.equal(userJoin.get("id"), userId);
		};
	}
	
	public static Specification<Task> teamIdIs(Long teamId) {
		return (root, query, cb) -> {
			Join<Task, User> userJoin = root.join("user", JoinType.INNER);
            Join<User, Team> grandChildUserJoin = userJoin.join("team", JoinType.LEFT);
			return cb.equal(grandChildUserJoin.get("id"), teamId);
		};
	}

	public static Specification<Task> statusIs(Integer status) {
	    return (root, query, cb) -> cb.equal(root.get("status"), status);
	}
	
	public static Specification<Task> statusLessThan(Integer status) {
	    return (root, query, cb) -> cb.lessThan(root.get("status"), status);
	}

	public static Specification<Task> dueDateAfter(Date date) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dueDate"), date);
	}

	public static Specification<Task> dueDateBefore(Date date) {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dueDate"), date);
	}
}
