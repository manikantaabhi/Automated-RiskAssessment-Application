package com.tool.AutomatedRiskAssessment.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tool.AutomatedRiskAssessment.model.User;

public interface UserRepository extends JpaRepository<User, String>{
	Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
