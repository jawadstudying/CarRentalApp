package com.example.carrentalapp.service;

import com.example.carrentalapp.dao.UserDAO;
import com.example.carrentalapp.model.User;

import java.sql.SQLException;
import java.util.List;

// Service for user (employee) operations
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public void addUser(User user) throws SQLException {
        userDAO.insert(user);
    }

    public void updateUser(User user) throws SQLException {
        userDAO.update(user);
    }

    public void deleteUser(int userId) throws SQLException {
        userDAO.delete(userId);
    }
}
