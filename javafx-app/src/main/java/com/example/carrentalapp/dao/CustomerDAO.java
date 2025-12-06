package com.example.carrentalapp.dao;

import com.example.carrentalapp.DBconfig;
import com.example.carrentalapp.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Data access for "customers" table
public class CustomerDAO {

    // Get all customers
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT customer_id, first_name, last_name, phone, email " +
                "FROM customers ORDER BY customer_id";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                Customer c = new Customer(customerId, firstName, lastName, phone, email);
                customers.add(c);
            }
        }

        return customers;
    }

    // Insert new customer
    public void insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (first_name, last_name, phone, email) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getEmail());

            ps.executeUpdate();
        }
    }

    // Update existing customer
    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customers " +
                "SET first_name = ?, last_name = ?, phone = ?, email = ? " +
                "WHERE customer_id = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getEmail());
            ps.setInt(5, customer.getCustomerId());

            ps.executeUpdate();
        }
    }

    // Delete customer by id
    public void delete(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection conn = DBconfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.executeUpdate();
        }
    }
}
