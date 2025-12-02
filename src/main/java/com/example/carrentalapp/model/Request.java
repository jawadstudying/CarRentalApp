package com.example.carrentalapp.model;

import java.time.LocalDateTime;

// Represents one row in "requests" table
public class Request {

    // Fields
    private int requestId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int carId;
    private LocalDateTime createdAt;

    // Constructors
    public Request() {
    }

    public Request(int requestId,
                   String firstName,
                   String lastName,
                   String phone,
                   String email,
                   int carId,
                   LocalDateTime createdAt) {
        this.requestId = requestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.carId = carId;
        this.createdAt = createdAt;
    }

    // Getters / setters
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // For display
    @Override
    public String toString() {
        return firstName + " " + lastName + " (car " + carId + ")";
    }
}
