package com.example.carrentalapp.service;

import com.example.carrentalapp.dao.UserDAO;
import com.example.carrentalapp.model.User;

import java.sql.SQLException;

// Handles login logic
public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    // Try to login, return User or null
    public User login(String username, String password) throws SQLException {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }

        // trim to avoid spaces issues
        String cleanUsername = username.trim();
        String cleanPassword = password.trim();

        return userDAO.findByUsernameAndPassword(cleanUsername, cleanPassword);
    }
}
