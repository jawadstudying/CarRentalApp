package com.example.carrentalapp.dao;

import com.example.carrentalapp.DBconfig;
import com.example.carrentalapp.model.MaintenanceRecord;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Data access for "maintenance_records" table
public class MaintenanceRecordDAO {

    public List<MaintenanceRecord> findAll() throws SQLException {
        List<MaintenanceRecord> records = new ArrayList<>();

        String sql = "SELECT maintenance_id, car_id, maintenance_date, description, cost " +
                "FROM maintenance_records ORDER BY maintenance_id";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("maintenance_id");
                int carId = rs.getInt("car_id");
                Date dateSql = rs.getDate("maintenance_date");
                LocalDate date = dateSql != null ? dateSql.toLocalDate() : null;
                String description = rs.getString("description");
                BigDecimal cost = rs.getBigDecimal("cost");

                MaintenanceRecord mr = new MaintenanceRecord();
                mr.setMaintenanceId(id);
                mr.setCarId(carId);
                mr.setMaintenanceDate(date);
                mr.setDescription(description);
                mr.setCost(cost);

                records.add(mr);
            }
        }

        return records;
    }

    public void insert(MaintenanceRecord record) throws SQLException {
        String sql = "INSERT INTO maintenance_records (car_id, maintenance_date, description, cost) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, record.getCarId());
            ps.setDate(2, Date.valueOf(record.getMaintenanceDate()));
            ps.setString(3, record.getDescription());
            ps.setBigDecimal(4, record.getCost());

            ps.executeUpdate();
        }
    }

    public void update(MaintenanceRecord record) throws SQLException {
        String sql = "UPDATE maintenance_records " +
                "SET car_id = ?, maintenance_date = ?, description = ?, cost = ? " +
                "WHERE maintenance_id = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, record.getCarId());
            ps.setDate(2, Date.valueOf(record.getMaintenanceDate()));
            ps.setString(3, record.getDescription());
            ps.setBigDecimal(4, record.getCost());
            ps.setInt(5, record.getMaintenanceId());

            ps.executeUpdate();
        }
    }

    public void delete(int maintenanceId) throws SQLException {
        String sql = "DELETE FROM maintenance_records WHERE maintenance_id = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maintenanceId);
            ps.executeUpdate();
        }
    }
}

