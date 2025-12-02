package com.example.carrentalapp.service;

import com.example.carrentalapp.dao.MaintenanceRecordDAO;
import com.example.carrentalapp.model.MaintenanceRecord;

import java.sql.SQLException;
import java.util.List;

// Service for maintenance records
public class MaintenanceService {

    private final MaintenanceRecordDAO maintenanceRecordDAO = new MaintenanceRecordDAO();

    public List<MaintenanceRecord> getAllRecords() throws SQLException {
        return maintenanceRecordDAO.findAll();
    }

    public void addRecord(MaintenanceRecord record) throws SQLException {
        maintenanceRecordDAO.insert(record);
    }

    public void updateRecord(MaintenanceRecord record) throws SQLException {
        maintenanceRecordDAO.update(record);
    }

    public void deleteRecord(int maintenanceId) throws SQLException {
        maintenanceRecordDAO.delete(maintenanceId);
    }
}
