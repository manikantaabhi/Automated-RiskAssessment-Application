package com.tool.AutomatedRiskAssessment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")  // Ensuring it maps to the "users" table in DB
public class User {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)  // Maps to "password" column in DB
    private String password;

    @Column(nullable = false)
    private String organization;

    // Default constructor
    public User() {}

    // All-args constructor
    public User(String firstName, String lastName, String phone, String email,
                String username, String password, String organization) {
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.organization = organization;
    }

    // Getters and Setters
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "User{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }
}
