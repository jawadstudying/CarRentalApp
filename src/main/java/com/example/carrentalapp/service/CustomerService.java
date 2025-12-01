package com.example.carrentalapp.service;

import com.example.carrentalapp.dao.CustomerDAO;
import com.example.carrentalapp.model.Customer;

import java.sql.SQLException;
import java.util.List;

// Service for customer operations
public class CustomerService {

    private final CustomerDAO customerDAO = new CustomerDAO();

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.findAll();
    }

    public void addCustomer(Customer customer) throws SQLException {
        customerDAO.insert(customer);
    }

    public void updateCustomer(Customer customer) throws SQLException {
        customerDAO.update(customer);
    }

    public void deleteCustomer(int customerId) throws SQLException {
        customerDAO.delete(customerId);
    }
}
