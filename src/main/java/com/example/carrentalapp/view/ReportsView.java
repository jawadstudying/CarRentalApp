package com.example.carrentalapp.view;

import com.example.carrentalapp.model.Car;
import com.example.carrentalapp.model.Rental;
import com.example.carrentalapp.service.CarService;
import com.example.carrentalapp.service.RentalService;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// Reports / Export screen
public class ReportsView {

    private final VBox root;
    private final Stage stage;

    private final CarService carService = new CarService();
    private final RentalService rentalService = new RentalService();

    private final DatePicker dpStart = new DatePicker();
    private final DatePicker dpEnd = new DatePicker();
    private final Label lblMessage = new Label();

    public ReportsView(Stage stage) {
        this.stage = stage;

        Label title = new Label("Reports / Export");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Export cars section
        Label carsLabel = new Label("Export all cars to CSV");
        Button btnExportCars = new Button("Export Cars");
        btnExportCars.setOnAction(e -> exportCars());

        VBox carsBox = new VBox(5, carsLabel, btnExportCars);
        carsBox.setPadding(new Insets(10));
        carsBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 4; -fx-border-width: 1;");

        // Export rentals section
        Label rentalsLabel = new Label("Export rentals by date range to CSV");
        dpStart.setPromptText("Start date");
        dpEnd.setPromptText("End date");

        Button btnExportRentals = new Button("Export Rentals");
        btnExportRentals.setOnAction(e -> exportRentalsByDate());

        VBox rentalsBox = new VBox(5, rentalsLabel, dpStart, dpEnd, btnExportRentals);
        rentalsBox.setPadding(new Insets(10));
        rentalsBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 4; -fx-border-width: 1;");

        root = new VBox(15, title, carsBox, rentalsBox, lblMessage);
        root.setPadding(new Insets(15));
    }

    public Parent getRoot() {
        return root;
    }

    private void exportCars() {
        try {
            List<Car> cars = carService.getAllCars();
            File target = chooseFile("cars_export.csv");
            if (target == null) {
                lblMessage.setText("Export cancelled.");
                return;
            }

            try (PrintWriter writer = new PrintWriter(target)) {
                writer.println("car_id,license_plate,brand,model,year,daily_rate,mileage,status");
                for (Car c : cars) {
                    writer.printf("%d,%s,%s,%s,%d,%s,%d,%s%n",
                            c.getCarId(),
                            escape(c.getLicensePlate()),
                            escape(c.getBrand()),
                            escape(c.getModel()),
                            c.getYear(),
                            c.getDailyRate() != null ? c.getDailyRate().toPlainString() : "",
                            c.getMileage(),
                            escape(c.getStatus()));
                }
            }

            lblMessage.setText("Cars exported to " + target.getAbsolutePath());
        } catch (SQLException | FileNotFoundException ex) {
            lblMessage.setText("Error exporting cars: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void exportRentalsByDate() {
        try {
            LocalDate startDate = dpStart.getValue();
            LocalDate endDate = dpEnd.getValue();

            if (startDate == null || endDate == null) {
                lblMessage.setText("Select both start and end dates.");
                return;
            }
            if (endDate.isBefore(startDate)) {
                lblMessage.setText("End date must be after or equal to start date.");
                return;
            }

            List<Rental> rentals = rentalService.getRentalsByDateRange(startDate, endDate);
            File target = chooseFile("rentals_export.csv");
            if (target == null) {
                lblMessage.setText("Export cancelled.");
                return;
            }

            try (PrintWriter writer = new PrintWriter(target)) {
                writer.println("rental_id,customer_id,car_id,created_by_user_id,start_date,end_date,daily_rate,total_amount,payment_status,payment_date,status");
                for (Rental r : rentals) {
                    writer.printf("%d,%d,%d,%d,%s,%s,%s,%s,%s,%s,%s%n",
                            r.getRentalId(),
                            r.getCustomerId(),
                            r.getCarId(),
                            r.getCreatedByUserId(),
                            formatDate(r.getStartDate()),
                            formatDate(r.getEndDate()),
                            formatBigDecimal(r.getDailyRate()),
                            formatBigDecimal(r.getTotalAmount()),
                            escape(r.getPaymentStatus()),
                            formatDate(r.getPaymentDate()),
                            escape(r.getStatus()));
                }
            }

            lblMessage.setText("Rentals exported to " + target.getAbsolutePath());
        } catch (SQLException | FileNotFoundException ex) {
            lblMessage.setText("Error exporting rentals: " + ex.getMessage());
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            lblMessage.setText(ex.getMessage());
        }
    }

    private File chooseFile(String suggestedName) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save CSV");
        chooser.setInitialFileName(suggestedName);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        return chooser.showSaveDialog(stage);
    }

    private static String formatDate(LocalDate date) {
        return date != null ? date.toString() : "";
    }

    private static String formatBigDecimal(java.math.BigDecimal value) {
        return value != null ? value.toPlainString() : "";
    }

    // Escape commas/quotes by surrounding with quotes if needed
    private static String escape(String val) {
        if (val == null) return "";
        boolean needsQuotes = val.contains(",") || val.contains("\"") || val.contains("\n");
        String escaped = val.replace("\"", "\"\"");
        return needsQuotes ? "\"" + escaped + "\"" : escaped;
    }
}
