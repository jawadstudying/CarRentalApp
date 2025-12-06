package com.example.carrentalapp.view;

import com.example.carrentalapp.model.Car;
import com.example.carrentalapp.service.CarService;
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
import java.util.List;

// Screen for managing cars
public class CarManagementView {

    private final BorderPane root;

    private final TableView<Car> tableView;
    private final TextField txtId;
    private final TextField txtLicensePlate;
    private final TextField txtBrand;
    private final TextField txtModel;
    private final TextField txtYear;
    private final TextField txtDailyRate;
    private final TextField txtMileage;
    private final ComboBox<String> cbStatus;
    private final Label lblMessage;

    private final CarService carService = new CarService();

    public CarManagementView() {
        root = new BorderPane();

        Label title = new Label("Car Management");
        HBox top = new HBox(title);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);

        tableView = new TableView<>();
        setupTableColumns();

        txtId = new TextField();
        txtId.setPromptText("ID");
        txtId.setEditable(false);

        txtLicensePlate = new TextField();
        txtLicensePlate.setPromptText("License Plate");

        txtBrand = new TextField();
        txtBrand.setPromptText("Brand");

        txtModel = new TextField();
        txtModel.setPromptText("Model");

        txtYear = new TextField();
        txtYear.setPromptText("Year");

        txtDailyRate = new TextField();
        txtDailyRate.setPromptText("Daily Rate");

        txtMileage = new TextField();
        txtMileage.setPromptText("Mileage");

        cbStatus = new ComboBox<>();
        cbStatus.getItems().addAll("AVAILABLE", "RENTED", "MAINTENANCE");
        cbStatus.setPromptText("Status");

        lblMessage = new Label();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        form.add(new Label("ID:"), 0, 0);
        form.add(txtId, 1, 0);

        form.add(new Label("License Plate:"), 2, 0);
        form.add(txtLicensePlate, 3, 0);

        form.add(new Label("Brand:"), 0, 1);
        form.add(txtBrand, 1, 1);

        form.add(new Label("Model:"), 2, 1);
        form.add(txtModel, 3, 1);

        form.add(new Label("Year:"), 0, 2);
        form.add(txtYear, 1, 2);

        form.add(new Label("Daily Rate:"), 2, 2);
        form.add(txtDailyRate, 3, 2);

        form.add(new Label("Mileage:"), 0, 3);
        form.add(txtMileage, 1, 3);

        form.add(new Label("Status:"), 2, 3);
        form.add(cbStatus, 3, 3);

        Button btnLoad = new Button("Load Cars");
        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnClear = new Button("Clear");

        btnLoad.setOnAction(e -> loadCars());
        btnAdd.setOnAction(e -> addCar());
        btnUpdate.setOnAction(e -> updateCar());
        btnDelete.setOnAction(e -> deleteCar());
        btnClear.setOnAction(e -> clearForm());

        HBox buttons = new HBox(10, btnLoad, btnAdd, btnUpdate, btnDelete, btnClear);
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
                (obs, oldSelection, newSelection) -> fillFormFromSelection(newSelection)
        );

        loadCars();
    }

    public Parent getRoot() {
        return root;
    }

    private void setupTableColumns() {
        TableColumn<Car, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("carId"));

        TableColumn<Car, String> colPlate = new TableColumn<>("License Plate");
        colPlate.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));

        TableColumn<Car, String> colBrand = new TableColumn<>("Brand");
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Car, String> colModel = new TableColumn<>("Model");
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Car, Integer> colYear = new TableColumn<>("Year");
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Car, BigDecimal> colRate = new TableColumn<>("Daily Rate");
        colRate.setCellValueFactory(new PropertyValueFactory<>("dailyRate"));

        TableColumn<Car, Integer> colMileage = new TableColumn<>("Mileage");
        colMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));

        TableColumn<Car, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().addAll(colId, colPlate, colBrand, colModel, colYear, colRate, colMileage, colStatus);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadCars() {
        try {
            List<Car> cars = carService.getAllCars();
            ObservableList<Car> data = FXCollections.observableArrayList(cars);
            tableView.setItems(data);
            lblMessage.setText("Cars loaded: " + cars.size());
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error loading cars: " + e.getMessage());
        }
    }

    private void addCar() {
        try {
            Car car = buildCarFromForm(false);
            if (car == null) {
                return;
            }

            carService.addCar(car);
            loadCars();
            clearForm();
            lblMessage.setText("Car added.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error adding car: " + e.getMessage());
        }
    }

    private void updateCar() {
        try {
            Car car = buildCarFromForm(true);
            if (car == null) {
                return;
            }

            carService.updateCar(car);
            loadCars();
            clearForm();
            lblMessage.setText("Car updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error updating car: " + e.getMessage());
        }
    }

    private void deleteCar() {
        try {
            String idText = txtId.getText();
            if (idText == null || idText.isBlank()) {
                lblMessage.setText("Select a car to delete.");
                return;
            }

            int carId = Integer.parseInt(idText);
            carService.deleteCar(carId);
            loadCars();
            clearForm();
            lblMessage.setText("Car deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error deleting car: " + e.getMessage());
        } catch (NumberFormatException e) {
            lblMessage.setText("Invalid ID format.");
        }
    }

    private void clearForm() {
        txtId.clear();
        txtLicensePlate.clear();
        txtBrand.clear();
        txtModel.clear();
        txtYear.clear();
        txtDailyRate.clear();
        txtMileage.clear();
        cbStatus.setValue(null);
        lblMessage.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    private void fillFormFromSelection(Car car) {
        if (car == null) {
            return;
        }

        txtId.setText(String.valueOf(car.getCarId()));
        txtLicensePlate.setText(car.getLicensePlate());
        txtBrand.setText(car.getBrand());
        txtModel.setText(car.getModel());
        txtYear.setText(String.valueOf(car.getYear()));
        txtDailyRate.setText(car.getDailyRate() != null ? car.getDailyRate().toString() : "");
        txtMileage.setText(String.valueOf(car.getMileage()));
        cbStatus.setValue(car.getStatus());
    }

    private Car buildCarFromForm(boolean requireId) {
        try {
            Integer id = null;
            if (requireId) {
                String idText = txtId.getText();
                if (idText == null || idText.isBlank()) {
                    lblMessage.setText("Select a car first.");
                    return null;
                }
                id = Integer.parseInt(idText);
            }

            String plate = txtLicensePlate.getText();
            String brand = txtBrand.getText();
            String model = txtModel.getText();
            String yearText = txtYear.getText();
            String rateText = txtDailyRate.getText();
            String mileageText = txtMileage.getText();
            String status = cbStatus.getValue();

            if (plate == null || plate.isBlank()
                    || brand == null || brand.isBlank()
                    || model == null || model.isBlank()
                    || yearText == null || yearText.isBlank()
                    || rateText == null || rateText.isBlank()
                    || mileageText == null || mileageText.isBlank()
                    || status == null || status.isBlank()) {
                lblMessage.setText("Fill all fields.");
                return null;
            }

            int year = Integer.parseInt(yearText);
            int mileage = Integer.parseInt(mileageText);
            BigDecimal rate = new BigDecimal(rateText);

            Car car = new Car();
            if (id != null) {
                car.setCarId(id);
            }
            car.setLicensePlate(plate.trim());
            car.setBrand(brand.trim());
            car.setModel(model.trim());
            car.setYear(year);
            car.setDailyRate(rate);
            car.setMileage(mileage);
            car.setStatus(status.trim());

            return car;

        } catch (NumberFormatException e) {
            lblMessage.setText("Year, daily rate, and mileage must be numbers.");
            return null;
        }
    }
}
