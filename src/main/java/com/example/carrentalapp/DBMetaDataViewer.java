package com.example.carrentalapp;

import java.sql.*;

public class DBMetaDataViewer {

    public static void main(String[] args) {
        try (Connection conn = DBconfig.getConnection()) {

            DatabaseMetaData meta = conn.getMetaData();

            // Get tables
            ResultSet tables = meta.getTables("rent_a_car_db", null, "%", new String[]{"TABLE"});


            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("TABLE: " + tableName);

                // Get columns of this table
                ResultSet columns = meta.getColumns(null, null, tableName, "%");

                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String type = columns.getString("TYPE_NAME");
                    String size = columns.getString("COLUMN_SIZE");
                    String nullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable ? "NULL" : "NOT NULL";

                    System.out.println("    " + columnName + "   " + type + "(" + size + ")   " + nullable);
                }

                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
