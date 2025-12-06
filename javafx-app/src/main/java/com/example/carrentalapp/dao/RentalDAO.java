package com.example.carrentalapp.dao;

import com.example.carrentalapp.DBconfig;
import com.example.carrentalapp.model.Rental;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Data access for "rentals" table + stored procedures
public class RentalDAO {

    // Get all rentals
    public List<Rental> findAll() throws SQLException {
        List<Rental> rentals = new ArrayList<>();

        String sql = "SELECT rental_id, customer_id, car_id, created_by_user_id, " +
                "start_date, end_date, daily_rate, total_amount, payment_status, payment_date, status " +
                "FROM rentals ORDER BY rental_id";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int rentalId = rs.getInt("rental_id");
                int customerId = rs.getInt("customer_id");
                int carId = rs.getInt("car_id");
                int createdByUserId = rs.getInt("created_by_user_id");

                Date startSql = rs.getDate("start_date");
                Date endSql = rs.getDate("end_date");
                Date paymentSql = rs.getDate("payment_date");

                LocalDate startDate = startSql != null ? startSql.toLocalDate() : null;
                LocalDate endDate = endSql != null ? endSql.toLocalDate() : null;
                LocalDate paymentDate = paymentSql != null ? paymentSql.toLocalDate() : null;

                BigDecimal dailyRate = rs.getBigDecimal("daily_rate");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                String paymentStatus = rs.getString("payment_status");
                String status = rs.getString("status");

                Rental r = new Rental();
                r.setRentalId(rentalId);
                r.setCustomerId(customerId);
                r.setCarId(carId);
                r.setCreatedByUserId(createdByUserId);
                r.setStartDate(startDate);
                r.setEndDate(endDate);
                r.setDailyRate(dailyRate);
                r.setTotalAmount(totalAmount);
                r.setPaymentStatus(paymentStatus);
                r.setPaymentDate(paymentDate);
                r.setStatus(status);

                rentals.add(r);
            }
        }

        return rentals;
    }

    // Get rentals between dates (inclusive)
    public List<Rental> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Rental> rentals = new ArrayList<>();

        String sql = "SELECT rental_id, customer_id, car_id, created_by_user_id, " +
                "start_date, end_date, daily_rate, total_amount, payment_status, payment_date, status " +
                "FROM rentals WHERE start_date >= ? AND end_date <= ? ORDER BY rental_id";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int rentalId = rs.getInt("rental_id");
                    int customerId = rs.getInt("customer_id");
                    int carId = rs.getInt("car_id");
                    int createdByUserId = rs.getInt("created_by_user_id");

                    Date startSql = rs.getDate("start_date");
                    Date endSql = rs.getDate("end_date");
                    Date paymentSql = rs.getDate("payment_date");

                    LocalDate sDate = startSql != null ? startSql.toLocalDate() : null;
                    LocalDate eDate = endSql != null ? endSql.toLocalDate() : null;
                    LocalDate paymentDate = paymentSql != null ? paymentSql.toLocalDate() : null;

                    BigDecimal dailyRate = rs.getBigDecimal("daily_rate");
                    BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                    String paymentStatus = rs.getString("payment_status");
                    String status = rs.getString("status");

                    Rental r = new Rental();
                    r.setRentalId(rentalId);
                    r.setCustomerId(customerId);
                    r.setCarId(carId);
                    r.setCreatedByUserId(createdByUserId);
                    r.setStartDate(sDate);
                    r.setEndDate(eDate);
                    r.setDailyRate(dailyRate);
                    r.setTotalAmount(totalAmount);
                    r.setPaymentStatus(paymentStatus);
                    r.setPaymentDate(paymentDate);
                    r.setStatus(status);

                    rentals.add(r);
                }
            }
        }

        return rentals;
    }

    // Call create_rental_simple procedure
    public void createRentalUsingProcedure(int customerId,
                                           int carId,
                                           int userId,
                                           LocalDate startDate,
                                           LocalDate endDate) throws SQLException {

        String sql = "{ CALL create_rental_simple(?, ?, ?, ?, ?) }";

        try (Connection conn = DBconfig.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, customerId);
            cs.setInt(2, carId);
            cs.setInt(3, userId);
            cs.setDate(4, Date.valueOf(startDate));
            cs.setDate(5, Date.valueOf(endDate));

            cs.execute();
        }
    }

    // Call return_rental_simple procedure
    public void returnRentalUsingProcedure(int rentalId,
                                           LocalDate returnDate) throws SQLException {

        String sql = "{ CALL return_rental_simple(?, ?) }";

        try (Connection conn = DBconfig.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, rentalId);
            cs.setDate(2, Date.valueOf(returnDate));

            cs.execute();
        }
    }

    // Update payment status (and payment_date when paid)
    public void updatePaymentStatus(int rentalId, String paymentStatus) throws SQLException {
        String sql = "UPDATE rentals SET payment_status = ?, payment_date = ? WHERE rental_id = ?";

        LocalDate paymentDate = "PAID".equalsIgnoreCase(paymentStatus) ? LocalDate.now() : null;

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            if (paymentDate != null) {
                ps.setDate(2, Date.valueOf(paymentDate));
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setInt(3, rentalId);

            ps.executeUpdate();
        }
    }
}
