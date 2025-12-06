package com.example.carrentalapp.dao;

import com.example.carrentalapp.DBconfig;
import com.example.carrentalapp.model.Request;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Data access for "requests" table
public class RequestDAO {

    // Get all requests
    public List<Request> findAll() throws SQLException {
        List<Request> requests = new ArrayList<>();

        String sql = "SELECT request_id, first_name, last_name, phone, email, car_id, created_at " +
                "FROM requests ORDER BY request_id";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int carId = rs.getInt("car_id");
                Timestamp createdTs = rs.getTimestamp("created_at");
                LocalDateTime createdAt = createdTs != null ? createdTs.toLocalDateTime() : null;

                Request r = new Request(requestId, firstName, lastName, phone, email, carId, createdAt);
                requests.add(r);
            }
        }

        return requests;
    }

    // Delete request by id
    public void delete(int requestId) throws SQLException {
        String sql = "DELETE FROM requests WHERE request_id = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ps.executeUpdate();
        }
    }
}
