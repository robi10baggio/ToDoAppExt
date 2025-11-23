package com.todo.app.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.app.entity.Team;
import com.todo.app.repository.TeamRepository;

@Service
public class TeamService {
	@Autowired 
	private TeamRepository teamRepository;
	

	public List<Team> findAllByOrderById() {
		return teamRepository.findAllByOrderById();
	}
	public Team findById(Long id) {
		return teamRepository.findById(id).orElse(null);
	}
	
	@Transactional
	public void regist(Team team) {
		teamRepository.save(team);
	}
	
	@Transactional
	public void update(Team team) {
		teamRepository.save(team);
	}
	
	@Transactional
	public void delete(long id) {
		teamRepository.deleteById(id);
	}
}
