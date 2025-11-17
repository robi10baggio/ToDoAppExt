package com.todo.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.app.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

	List<Team> findAllByOrderById();

}
