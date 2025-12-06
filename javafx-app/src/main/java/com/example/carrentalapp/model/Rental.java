package com.example.carrentalapp.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Represents one row in "rentals" table
public class Rental {

    // Fields
    private int rentalId;
    private int customerId;
    private int carId;
    private int createdByUserId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal dailyRate;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private LocalDate paymentDate;
    private String status;

    // Constructors
    public Rental() {
    }

    public Rental(int rentalId,
                  int customerId,
                  int carId,
                  int createdByUserId,
                  LocalDate startDate,
                  LocalDate endDate,
                  BigDecimal dailyRate,
                  BigDecimal totalAmount,
                  String paymentStatus,
                  LocalDate paymentDate,
                  String status) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.carId = carId;
        this.createdByUserId = createdByUserId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyRate = dailyRate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    // Getters / setters
    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Helpers
    public boolean isOngoing() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    public boolean isPaid() {
        return "PAID".equalsIgnoreCase(paymentStatus);
    }
}
