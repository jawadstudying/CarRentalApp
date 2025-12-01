package com.example.carrentalapp.model;

import java.math.BigDecimal;

// Represents one row in "cars" table
public class Car {

    // Fields
    private int carId;
    private String licensePlate;
    private String brand;
    private String model;
    private int year;
    private BigDecimal dailyRate;
    private int mileage;
    private String status;

    // Constructors
    public Car() {
    }

    public Car(int carId,
               String licensePlate,
               String brand,
               String model,
               int year,
               BigDecimal dailyRate,
               int mileage,
               String status) {
        this.carId = carId;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
        this.mileage = mileage;
        this.status = status;
    }

    // Getters / setters
    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // For display
    @Override
    public String toString() {
        return licensePlate + " - " + brand + " " + model;
    }

    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(status);
    }
}
