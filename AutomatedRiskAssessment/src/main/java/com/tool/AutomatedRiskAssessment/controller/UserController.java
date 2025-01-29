package com.tool.AutomatedRiskAssessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tool.AutomatedRiskAssessment.dto.LoginRequest;
import com.tool.AutomatedRiskAssessment.dto.SignupRequest;
import com.tool.AutomatedRiskAssessment.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> validateUser(@RequestBody LoginRequest loginRequest) {
        boolean isValid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (isValid) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequest signupRequest) {
        boolean isRegistered = userService.registerUser(signupRequest);
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
    }
    @GetMapping("/")
    public String checking() {
    	return "hello";
    }
    @PostMapping("/add")
    public String addName() {
    	return "added";
    }
}
