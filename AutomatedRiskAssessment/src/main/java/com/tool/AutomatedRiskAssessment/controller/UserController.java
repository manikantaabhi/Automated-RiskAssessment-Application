package com.tool.AutomatedRiskAssessment.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tool.AutomatedRiskAssessment.dto.LoginRequest;
import com.tool.AutomatedRiskAssessment.dto.SignupRequest;
import com.tool.AutomatedRiskAssessment.model.User;
import com.tool.AutomatedRiskAssessment.service.UserService;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> validateUser(@RequestBody LoginRequest loginRequest) {
    	System.out.println("hittting login");
        boolean isValid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (isValid) {
        	System.out.println("Login successful");
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password") );
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody SignupRequest signupRequest) {
    	System.out.println("hitting api");
    	System.out.println(signupRequest.toString());
        boolean isRegistered = userService.registerUser(signupRequest);
        if (isRegistered) {
        	System.out.println("User registered successfully");
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "already exists"));
        }
    }
    @GetMapping("/allusers")
    public List<User> getAllUsers() {
    	System.out.println("hitting api");
    	//System.out.println(signupRequest.toString());
       List<User> x= userService.getAllUsers();
       System.out.println(x);
       return x;
        
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
