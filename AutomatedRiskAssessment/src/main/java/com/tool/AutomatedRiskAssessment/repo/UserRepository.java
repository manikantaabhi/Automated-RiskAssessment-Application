package com.tool.AutomatedRiskAssessment.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tool.AutomatedRiskAssessment.model.User;

public interface UserRepository extends JpaRepository<User, String>{
	Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u.email FROM User u")
    List<String> findAllEmails();
    
    @Query("SELECT u.email FROM User u WHERE u.username = :username")
    Optional<String> findEmailByUsername(String username);

}
