package com.tool.AutomatedRiskAssessment.service;

import com.tool.AutomatedRiskAssessment.dto.SignupRequest;
import com.tool.AutomatedRiskAssessment.model.User;
import com.tool.AutomatedRiskAssessment.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender emailSender;  // Inject email sender for sending email

    //private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        newUser.setEmail(signupRequest.getEmail());
        newUser.setUsername(signupRequest.getUsername());
        newUser.setPassword(signupRequest.getPassword());
        newUser.setOrganization(signupRequest.getOrganization());
        userRepository.save(newUser);
        return true;
    }

    // Method to send password reset link to the user's email
    // Logic to send email (e.g., reset password link)
    public boolean sendPasswordResetLink(String email) {
        // Check if the user exists, for example, using email (you can customize this)
        // For simplicity, let's assume the user exists

        // Create the email content
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("Hi, \n\nClick the link below to reset your password:\n\nhttp://localhost:4200/reset-password");

        try {
            // Send the email
            emailSender.send(message);
            return true;
        } catch (Exception e) {
            // Log error and return false if email sending fails
            System.out.println("Error sending email: " + e.getMessage());
            return false;
        }
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Method to update password
    public boolean updatePassword(String username, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(newPassword);  // Updates the password column
            userRepository.save(user);  // Save updated password in the database
            return true;
        }
        return false;  // User not found
    }

    // NEW METHOD: Retrieve user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String getEmailByUserName(String userName) {
        return userRepository.findByUsername(userName).get().getEmail();
    }
}