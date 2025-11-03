package com.todo.app.model;

import java.util.Date;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.todo.app.entity.Team;
import com.todo.app.entity.Todo;
import com.todo.app.entity.User;

public class TodoSpecifications {
	public static Specification<Todo> taskContains(String keyword) {
		return (root, query, cb) -> cb.like(root.get("taskContent"), "%" + keyword + "%");
	}

	public static Specification<Todo> userIdIs(Long userId) {
		return (root, query, cb) -> {
			Join<Todo, User> join = root.join("user", JoinType.INNER);
			return cb.equal(root.get("id"),userId);
		};
	}
	
	public static Specification<Todo> teamIdIs(Long teamId) {
		return (root, query, cb) -> {
			Join<Todo, Team> join = root.join("team", JoinType.INNER);
			return cb.equal(root.get("id"), teamId);
		};
	}

	public static Specification<Todo> statusIs(Integer status) {
	    return (root, query, cb) -> cb.equal(root.get("status"), status);
	}
	
	public static Specification<Todo> statusLessThan(Integer status) {
	    return (root, query, cb) -> cb.lessThan(root.get("status"), status);
	}

	public static Specification<Todo> dueDateAfter(Date date) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timeLimit"), date);
	}

	public static Specification<Todo> dueDateBefore(Date date) {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("timeLimit"), date);
	}
}
