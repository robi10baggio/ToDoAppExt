package com.todo.app.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.app.entity.User;
import com.todo.app.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository loginRepository;
	
	public User loginAuth(String userName, String password) {
		return loginRepository.findByUserIdAndPassword(userName, password);
	}

	public List<User> findAllByOrderById() {
		return loginRepository.findAllByOrderById();
	}
	
	public User findById(Long id) {
		return loginRepository.findById(id).orElse(null);
	}
	
	public User findByUserName(String userName) {
		return loginRepository.findByUserName(userName);
	}
	
	public List<User> findByTeamId(Long teamId) {
		return loginRepository.findByTeamId(teamId);
	}
	
	
	@Transactional
	public void regist(User user) {
		loginRepository.save(user);
	}
	
	@Transactional
	public void update(User user) {
		loginRepository.save(user);
	}
	
	@Transactional
	public void delete(long id) {
		loginRepository.deleteById(id);
	}
	
}
