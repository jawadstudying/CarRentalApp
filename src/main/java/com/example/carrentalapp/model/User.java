package com.example.carrentalapp.model;

import java.math.BigDecimal;

// Represents one row in "users" table
public class User {

    // Fields
    private int userId;
    private String username;
    private String passwordHash;
    private String role;
    private BigDecimal salary;

    // Constructors
    public User() {
    }

    public User(int userId, String username, String passwordHash, String role, BigDecimal salary) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.salary = salary;
    }

    // Getters / setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    // For display
    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}
