package com.tool.AutomatedRiskAssessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tool.AutomatedRiskAssessment.dto.LoginRequest;
import com.tool.AutomatedRiskAssessment.dto.SignupRequest;
import com.tool.AutomatedRiskAssessment.model.User;
import com.tool.AutomatedRiskAssessment.service.EmailService;
import com.tool.AutomatedRiskAssessment.service.UserService;
import com.tool.AutomatedRiskAssessment.dto.ResetRequest;

import jakarta.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> validateUser(@RequestBody LoginRequest loginRequest) throws MessagingException {
        System.out.println("hitting login");
        boolean isValid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (isValid) {
            System.out.println("Login successful");
            String email = userService.getEmailByUserName(loginRequest.getUsername());
            String otp = emailService.generateOtp(email);
            emailService.sendOtpEmail(email, otp);
            return ResponseEntity.ok(Map.of("message", "User logged in successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
        }
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> request) {
        String userName = request.get("username");
        String email = userService.getEmailByUserName(userName);
        String otp = request.get("otp");
        boolean isValid = emailService.validateOtp(email, otp);
        if (isValid) {
            // Retrieve the User object to get the userId
            Optional<User> userOpt = userService.getUserByUsername(userName);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("userId", user.getId());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "User not found"));
            }
        } else {
            return ResponseEntity.ok(Map.of("success", false));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "User already exists"));
        }
    }

    @GetMapping("/allusers")
    public List<User> getAllUsers() {
        System.out.println("hitting api");
        List<User> x = userService.getAllUsers();
        System.out.println(x);
        return x;
    }

    // Updated GET endpoint: Query by username or return all users if username not provided
    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String username) {
        if (username != null && !username.isEmpty()) {
            Optional<User> userOpt = userService.getUserByUsername(username);
            if (userOpt.isPresent()) {
                return ResponseEntity.ok(userOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with username: " + username);
            }
        } else {
            return ResponseEntity.ok(userService.getAllUsers());
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

    // New endpoint for password reset
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> sendResetLink(@RequestBody ResetRequest resetRequest) {
        String email = resetRequest.getEmail();
        boolean emailSent = userService.sendPasswordResetLink(email);

        Map<String, String> response = new HashMap<>();
        if (emailSent) {
            response.put("message", "Password reset link is sent to your mail ID. Kindly check your mail.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Email not found or invalid");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> updatePassword(@RequestBody ResetRequest resetRequest) {
        String username = resetRequest.getUsername();
        System.out.println("hitting reset api");
        String newPassword = resetRequest.getNewPassword();
        String confirmPassword = resetRequest.getConfirmPassword();
        System.out.println(newPassword + " " + confirmPassword);
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match.");
        }
        boolean isUpdated = userService.updatePassword(username, newPassword);
        if (isUpdated) {
            System.out.println("Password updated successfully.");
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Password updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: User not found or password update failed.");
        }
    }
}