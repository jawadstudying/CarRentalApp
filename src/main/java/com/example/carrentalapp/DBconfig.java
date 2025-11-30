package com.example.carrentalapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconfig{

    private static final String URL =
            "jdbc:mysql://localhost:3306/rent_a_car_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";        // same as before
    private static final String PASSWORD = "";        // same as before

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
