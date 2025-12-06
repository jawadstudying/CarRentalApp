package com.example.carrentalapp.view;

import com.example.carrentalapp.model.Rental;
import com.example.carrentalapp.service.RentalService;
import com.example.carrentalapp.util.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// Screen for managing rentals
public class RentalManagementView {

    private final BorderPane root;

    private final TableView<Rental> tableView;
    private final TextField txtRentalId;
    private final TextField txtCustomerId;
    private final TextField txtCarId;
    private final DatePicker dpStartDate;
    private final DatePicker dpEndDate;
    private final DatePicker dpReturnDate;
    private final ComboBox<String> cbPaymentStatus;
    private final Label lblMessage;

    private final RentalService rentalService = new RentalService();

    public RentalManagementView() {
        root = new BorderPane();

        Label title = new Label("Rental Management");
        HBox top = new HBox(title);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);

        tableView = new TableView<>();
        setupTableColumns();

        txtRentalId = new TextField();
        txtRentalId.setPromptText("Rental ID");
        txtRentalId.setEditable(false);

        txtCustomerId = new TextField();
        txtCustomerId.setPromptText("Customer ID");

        txtCarId = new TextField();
        txtCarId.setPromptText("Car ID");

        dpStartDate = new DatePicker();
        dpStartDate.setPromptText("Start Date");

        dpEndDate = new DatePicker();
        dpEndDate.setPromptText("End Date");

        dpReturnDate = new DatePicker();
        dpReturnDate.setPromptText("Return Date");

        cbPaymentStatus = new ComboBox<>();
        cbPaymentStatus.getItems().addAll("UNPAID", "PAID");
        cbPaymentStatus.setPromptText("Payment Status");

        lblMessage = new Label();

        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Rental ID:"), 0, 0);
        form.add(txtRentalId, 1, 0);

        form.add(new Label("Customer ID:"), 0, 1);
        form.add(txtCustomerId, 1, 1);

        form.add(new Label("Car ID:"), 2, 1);
        form.add(txtCarId, 3, 1);

        form.add(new Label("Start Date:"), 0, 2);
        form.add(dpStartDate, 1, 2);

        form.add(new Label("End Date:"), 2, 2);
        form.add(dpEndDate, 3, 2);

        form.add(new Label("Return Date:"), 0, 3);
        form.add(dpReturnDate, 1, 3);
        form.add(new Label("Payment Status:"), 2, 3);
        form.add(cbPaymentStatus, 3, 3);

        Button btnLoad = new Button("Load Rentals");
        Button btnCreate = new Button("Create Rental");
        Button btnReturn = new Button("Return Rental");
        Button btnUpdatePayment = new Button("Update Payment");
        Button btnClear = new Button("Clear");

        btnLoad.setOnAction(e -> loadRentals());
        btnCreate.setOnAction(e -> createRental());
        btnReturn.setOnAction(e -> returnRental());
        btnUpdatePayment.setOnAction(e -> updatePaymentStatus());
        btnClear.setOnAction(e -> clearForm());

        HBox buttons = new HBox(10, btnLoad, btnCreate, btnReturn, btnUpdatePayment, btnClear);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(10));

        BorderPane bottom = new BorderPane();
        bottom.setTop(form);
        bottom.setCenter(buttons);
        bottom.setBottom(lblMessage);
        BorderPane.setMargin(lblMessage, new Insets(10));

        root.setTop(top);
        root.setCenter(tableView);
        root.setBottom(bottom);

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> fillFormFromSelection(newSel)
        );

        loadRentals();
    }

    public Parent getRoot() {
        return root;
    }

    private void setupTableColumns() {
        TableColumn<Rental, Integer> colId = new TableColumn<>("Rental ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));

        TableColumn<Rental, Integer> colCustomerId = new TableColumn<>("Customer ID");
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        TableColumn<Rental, Integer> colCarId = new TableColumn<>("Car ID");
        colCarId.setCellValueFactory(new PropertyValueFactory<>("carId"));

        TableColumn<Rental, Integer> colCreatedBy = new TableColumn<>("Created By (User)");
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdByUserId"));

        TableColumn<Rental, LocalDate> colStart = new TableColumn<>("Start Date");
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Rental, LocalDate> colEnd = new TableColumn<>("End Date");
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<Rental, BigDecimal> colTotal = new TableColumn<>("Total Amount");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        TableColumn<Rental, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Rental, String> colPaymentStatus = new TableColumn<>("Payment Status");
        colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        tableView.getColumns().addAll(colId, colCustomerId, colCarId, colCreatedBy,
                colStart, colEnd, colTotal, colPaymentStatus, colStatus);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadRentals() {
        try {
            List<Rental> rentals = rentalService.getAllRentals();
            ObservableList<Rental> data = FXCollections.observableArrayList(rentals);
            tableView.setItems(data);
            lblMessage.setText("Rentals loaded: " + rentals.size());
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error loading rentals: " + e.getMessage());
        }
    }

    private void createRental() {
        try {
            String customerText = txtCustomerId.getText();
            String carText = txtCarId.getText();

            if (customerText == null || customerText.isBlank()
                    || carText == null || carText.isBlank()) {
                lblMessage.setText("Customer ID and Car ID are required.");
                return;
            }

            int customerId = Integer.parseInt(customerText);
            int carId = Integer.parseInt(carText);

            LocalDate startDate = dpStartDate.getValue();
            LocalDate endDate = dpEndDate.getValue();

            if (Session.getCurrentUser() == null) {
                lblMessage.setText("You must be logged in to create a rental.");
                return;
            }

            int userId = Session.getCurrentUser().getUserId();

            rentalService.createRental(customerId, carId, userId, startDate, endDate);
            loadRentals();
            clearForm();
            lblMessage.setText("Rental created.");
        } catch (NumberFormatException ex) {
            lblMessage.setText("Customer ID and Car ID must be numbers.");
        } catch (IllegalArgumentException ex) {
            lblMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblMessage.setText("DB error: " + ex.getMessage());
        }
    }

    private void returnRental() {
        try {
            String rentalText = txtRentalId.getText();
            if (rentalText == null || rentalText.isBlank()) {
                lblMessage.setText("Select a rental or enter Rental ID.");
                return;
            }

            int rentalId = Integer.parseInt(rentalText);
            LocalDate returnDate = dpReturnDate.getValue();
            if (returnDate == null) {
                returnDate = LocalDate.now(); // default to today if not chosen
            }

            rentalService.returnRental(rentalId, returnDate);
            loadRentals();
            clearForm();
            lblMessage.setText("Rental returned.");
        } catch (NumberFormatException ex) {
            lblMessage.setText("Rental ID must be a number.");
        } catch (IllegalArgumentException ex) {
            lblMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblMessage.setText("DB error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtRentalId.clear();
        txtCustomerId.clear();
        txtCarId.clear();
        dpStartDate.setValue(null);
        dpEndDate.setValue(null);
        dpReturnDate.setValue(null);
        cbPaymentStatus.setValue(null);
        lblMessage.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    private void fillFormFromSelection(Rental r) {
        if (r == null) return;

        txtRentalId.setText(String.valueOf(r.getRentalId()));
        txtCustomerId.setText(String.valueOf(r.getCustomerId()));
        txtCarId.setText(String.valueOf(r.getCarId()));
        dpStartDate.setValue(r.getStartDate());
        dpEndDate.setValue(r.getEndDate());
        cbPaymentStatus.setValue(r.getPaymentStatus());
    }

    private void updatePaymentStatus() {
        try {
            String rentalText = txtRentalId.getText();
            if (rentalText == null || rentalText.isBlank()) {
                lblMessage.setText("Select a rental or enter Rental ID.");
                return;
            }

            String paymentStatus = cbPaymentStatus.getValue();
            if (paymentStatus == null || paymentStatus.isBlank()) {
                lblMessage.setText("Choose a payment status (PAID/UNPAID).");
                return;
            }

            int rentalId = Integer.parseInt(rentalText);
            rentalService.updatePaymentStatus(rentalId, paymentStatus);
            loadRentals();
            lblMessage.setText("Payment status updated.");
        } catch (NumberFormatException ex) {
            lblMessage.setText("Rental ID must be a number.");
        } catch (IllegalArgumentException ex) {
            lblMessage.setText(ex.getMessage());
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblMessage.setText("DB error: " + ex.getMessage());
        }
    }
}

