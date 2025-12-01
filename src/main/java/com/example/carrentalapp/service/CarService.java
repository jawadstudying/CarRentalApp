package com.example.carrentalapp.service;

import com.example.carrentalapp.dao.CarDAO;
import com.example.carrentalapp.model.Car;

import java.sql.SQLException;
import java.util.List;

// Service for car operations
public class CarService {

    private final CarDAO carDAO = new CarDAO();

    public List<Car> getAllCars() throws SQLException {
        return carDAO.findAll();
    }

    public void addCar(Car car) throws SQLException {
        carDAO.insert(car);
    }

    public void updateCar(Car car) throws SQLException {
        carDAO.update(car);
    }

    public void deleteCar(int carId) throws SQLException {
        carDAO.delete(carId);
    }
}
