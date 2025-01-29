package com.tool.AutomatedRiskAssessment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tool.AutomatedRiskAssessment.dto.SignupRequest;
import com.tool.AutomatedRiskAssessment.model.User;
import com.tool.AutomatedRiskAssessment.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Login validation
    public boolean validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    // Signup logic
    public boolean registerUser(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            return false; // User already exists
        }

        User newUser = new User();
        newUser.setFirstName(signupRequest.getFirstName());
        newUser.setLastName(signupRequest.getLastName());
        newUser.setPhone(signupRequest.getPhone());
        newUser.setEmail(signupRequest.getEmail());
        newUser.setUsername(signupRequest.getUsername());
        newUser.setPassword(signupRequest.getPassword());
        newUser.setOrganization(signupRequest.getOrganization());
        userRepository.save(newUser);
        return true;
    }
    public List<User> getAllUsers()
    {
    	return userRepository.findAll();
    }
}
