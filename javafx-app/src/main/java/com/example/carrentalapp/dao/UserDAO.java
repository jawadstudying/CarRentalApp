package com.example.carrentalapp.dao;

import com.example.carrentalapp.DBconfig;
import com.example.carrentalapp.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Data access for "users" table
public class UserDAO {

    // Used by AuthService for login
    public User findByUsernameAndPassword(String username, String password) throws SQLException {

        String sql = "SELECT user_id, username, password_hash, role, salary " +
                "FROM users WHERE username = ? AND password_hash = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String uname = rs.getString("username");
                    String pwdHash = rs.getString("password_hash");
                    String role = rs.getString("role");
                    BigDecimal salary = rs.getBigDecimal("salary");

                    return new User(userId, uname, pwdHash, role, salary);
                }
            }
        }

        return null;
    }

    // Get all users
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();

        String sql = "SELECT user_id, username, password_hash, role, salary " +
                "FROM users ORDER BY user_id";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String pwdHash = rs.getString("password_hash");
                String role = rs.getString("role");
                BigDecimal salary = rs.getBigDecimal("salary");

                User u = new User(userId, username, pwdHash, role, salary);
                users.add(u);
            }
        }

        return users;
    }

    // Insert new user
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, role, salary) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());
            ps.setBigDecimal(4, user.getSalary());

            ps.executeUpdate();
        }
    }

    // Update existing user
    public void update(User user) throws SQLException {
        String sql = "UPDATE users " +
                "SET username = ?, password_hash = ?, role = ?, salary = ? " +
                "WHERE user_id = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());
            ps.setBigDecimal(4, user.getSalary());
            ps.setInt(5, user.getUserId());

            ps.executeUpdate();
        }
    }

    // Delete user by id
    public void delete(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}
