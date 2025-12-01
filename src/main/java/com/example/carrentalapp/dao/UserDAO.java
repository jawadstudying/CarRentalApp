package com.example.carrentalapp.dao;

import com.example.carrentalapp.DBconfig;
import com.example.carrentalapp.model.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Handles database access for the "users" table
public class UserDAO {

    // Find one user by username and password
    public User findByUsernameAndPassword(String username, String password) throws SQLException {

        String sql = """
            SELECT user_id, username, password_hash, role, salary
            FROM users
            WHERE username = ? AND password_hash = ?
        """;

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password); // for now assume stored as plain or same hash

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

        return null; // not found
    }
}
