package com.example.carrentalapp.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Represents one row in "maintenance_records" table
public class MaintenanceRecord {

    // Fields
    private int maintenanceId;
    private int carId;
    private LocalDate maintenanceDate;
    private String description;
    private BigDecimal cost;

    // Constructors
    public MaintenanceRecord() {
    }

    public MaintenanceRecord(int maintenanceId,
                             int carId,
                             LocalDate maintenanceDate,
                             String description,
                             BigDecimal cost) {
        this.maintenanceId = maintenanceId;
        this.carId = carId;
        this.maintenanceDate = maintenanceDate;
        this.description = description;
        this.cost = cost;
    }

    // Getters / setters
    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    // For display
    @Override
    public String toString() {
        return "Maintenance on " + maintenanceDate + " (cost: " + cost + ")";
    }
}
