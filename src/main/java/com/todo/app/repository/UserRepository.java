package com.todo.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.app.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByUserIdAndPassword(String userId, String password);
	
	public User findByUserName(String userName);
	
	public List<User> findByTeamId(Long teamId);
}
