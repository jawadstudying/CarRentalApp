package com.example.carrentalapp.service;

import com.example.carrentalapp.dao.RentalDAO;
import com.example.carrentalapp.model.Rental;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// Service for rental operations
public class RentalService {

    private final RentalDAO rentalDAO = new RentalDAO();

    public List<Rental> getAllRentals() throws SQLException {
        return rentalDAO.findAll();
    }

    public List<Rental> getRentalsByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after or equal to start date.");
        }
        return rentalDAO.findByDateRange(startDate, endDate);
    }

    public void createRental(int customerId,
                             int carId,
                             int userId,
                             LocalDate startDate,
                             LocalDate endDate) throws SQLException {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates are required.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after or equal to start date.");
        }

        rentalDAO.createRentalUsingProcedure(customerId, carId, userId, startDate, endDate);
    }

    public void returnRental(int rentalId, LocalDate returnDate) throws SQLException {
        if (returnDate == null) {
            throw new IllegalArgumentException("Return date is required.");
        }
        rentalDAO.returnRentalUsingProcedure(rentalId, returnDate);
    }

    public void updatePaymentStatus(int rentalId, String paymentStatus) throws SQLException {
        if (rentalId <= 0) {
            throw new IllegalArgumentException("Rental ID is required.");
        }
        if (paymentStatus == null || paymentStatus.isBlank()) {
            throw new IllegalArgumentException("Payment status is required.");
        }

        String normalized = paymentStatus.trim().toUpperCase();
        if (!"PAID".equals(normalized) && !"UNPAID".equals(normalized)) {
            throw new IllegalArgumentException("Payment status must be PAID or UNPAID.");
        }

        rentalDAO.updatePaymentStatus(rentalId, normalized);
    }
}
