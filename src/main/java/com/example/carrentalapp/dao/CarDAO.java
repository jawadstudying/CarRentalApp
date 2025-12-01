package com.example.carrentalapp.dao;

import com.example.carrentalapp.DBconfig;
import com.example.carrentalapp.model.Car;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Data access for "cars" table
public class CarDAO {

    // Get all cars
    public List<Car> findAll() throws SQLException {
        List<Car> cars = new ArrayList<>();

        String sql = "SELECT car_id, license_plate, brand, model, year, daily_rate, mileage, status " +
                "FROM cars ORDER BY car_id";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int carId = rs.getInt("car_id");
                String licensePlate = rs.getString("license_plate");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                BigDecimal dailyRate = rs.getBigDecimal("daily_rate");
                int mileage = rs.getInt("mileage");
                String status = rs.getString("status");

                Car car = new Car(carId, licensePlate, brand, model, year, dailyRate, mileage, status);
                cars.add(car);
            }
        }

        return cars;
    }

    // Insert new car
    public void insert(Car car) throws SQLException {
        String sql = "INSERT INTO cars (license_plate, brand, model, year, daily_rate, mileage, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, car.getLicensePlate());
            ps.setString(2, car.getBrand());
            ps.setString(3, car.getModel());
            ps.setInt(4, car.getYear());
            ps.setBigDecimal(5, car.getDailyRate());
            ps.setInt(6, car.getMileage());
            ps.setString(7, car.getStatus());

            ps.executeUpdate();
        }
    }

    // Update existing car
    public void update(Car car) throws SQLException {
        String sql = "UPDATE cars " +
                "SET license_plate = ?, brand = ?, model = ?, year = ?, daily_rate = ?, mileage = ?, status = ? " +
                "WHERE car_id = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, car.getLicensePlate());
            ps.setString(2, car.getBrand());
            ps.setString(3, car.getModel());
            ps.setInt(4, car.getYear());
            ps.setBigDecimal(5, car.getDailyRate());
            ps.setInt(6, car.getMileage());
            ps.setString(7, car.getStatus());
            ps.setInt(8, car.getCarId());

            ps.executeUpdate();
        }
    }

    // Delete car by id
    public void delete(int carId) throws SQLException {
        String sql = "DELETE FROM cars WHERE car_id = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, carId);
            ps.executeUpdate();
        }
    }
}
