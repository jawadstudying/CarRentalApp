package com.example.carrentalapp.service;

import com.example.carrentalapp.dao.UserDAO;
import com.example.carrentalapp.model.User;
import com.example.carrentalapp.util.Session;

import java.sql.SQLException;
import java.util.List;

// Service for user (employee) operations
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() throws SQLException {
        ensureAdmin();
        return userDAO.findAll();
    }

    public void addUser(User user) throws SQLException {
        ensureAdmin();
        userDAO.insert(user);
    }

    public void updateUser(User user) throws SQLException {
        ensureAdmin();
        userDAO.update(user);
    }

    public void deleteUser(int userId) throws SQLException {
        ensureAdmin();
        userDAO.delete(userId);
    }

    private void ensureAdmin() {
        var currentUser = Session.getCurrentUser();
        if (currentUser == null || !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            throw new SecurityException("Only admins can manage users.");
        }
    }
}
