package com.example.carrentalapp.view;

import com.example.carrentalapp.model.MaintenanceRecord;
import com.example.carrentalapp.service.MaintenanceService;
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

// Screen for managing maintenance records
public class MaintenanceManagementView {

    private final BorderPane root;

    private final TableView<MaintenanceRecord> tableView;
    private final TextField txtId;
    private final TextField txtCarId;
    private final DatePicker dpDate;
    private final TextField txtDescription;
    private final TextField txtCost;
    private final Label lblMessage;

    private final MaintenanceService maintenanceService = new MaintenanceService();

    public MaintenanceManagementView() {
        root = new BorderPane();

        Label title = new Label("Maintenance Management");
        HBox top = new HBox(title);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);

        tableView = new TableView<>();
        setupTableColumns();

        txtId = new TextField();
        txtId.setPromptText("ID");
        txtId.setEditable(false);

        txtCarId = new TextField();
        txtCarId.setPromptText("Car ID");

        dpDate = new DatePicker();
        dpDate.setPromptText("Date");

        txtDescription = new TextField();
        txtDescription.setPromptText("Description");

        txtCost = new TextField();
        txtCost.setPromptText("Cost");

        lblMessage = new Label();

        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("ID:"), 0, 0);
        form.add(txtId, 1, 0);

        form.add(new Label("Car ID:"), 0, 1);
        form.add(txtCarId, 1, 1);

        form.add(new Label("Date:"), 2, 1);
        form.add(dpDate, 3, 1);

        form.add(new Label("Description:"), 0, 2);
        form.add(txtDescription, 1, 2, 3, 1);

        form.add(new Label("Cost:"), 0, 3);
        form.add(txtCost, 1, 3);

        Button btnLoad = new Button("Load Records");
        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnClear = new Button("Clear");

        btnLoad.setOnAction(e -> loadRecords());
        btnAdd.setOnAction(e -> addRecord());
        btnUpdate.setOnAction(e -> updateRecord());
        btnDelete.setOnAction(e -> deleteRecord());
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
                (obs, oldSel, newSel) -> fillFormFromSelection(newSel)
        );

        loadRecords();
    }

    public Parent getRoot() {
        return root;
    }

    private void setupTableColumns() {
        TableColumn<MaintenanceRecord, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("maintenanceId"));

        TableColumn<MaintenanceRecord, Integer> colCarId = new TableColumn<>("Car ID");
        colCarId.setCellValueFactory(new PropertyValueFactory<>("carId"));

        TableColumn<MaintenanceRecord, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("maintenanceDate"));

        TableColumn<MaintenanceRecord, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<MaintenanceRecord, BigDecimal> colCost = new TableColumn<>("Cost");
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        tableView.getColumns().addAll(colId, colCarId, colDate, colDesc, colCost);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadRecords() {
        try {
            List<MaintenanceRecord> records = maintenanceService.getAllRecords();
            ObservableList<MaintenanceRecord> data = FXCollections.observableArrayList(records);
            tableView.setItems(data);
            lblMessage.setText("Records loaded: " + records.size());
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error loading records: " + e.getMessage());
        }
    }

    private void addRecord() {
        try {
            MaintenanceRecord record = buildRecordFromForm(false);
            if (record == null) return;

            maintenanceService.addRecord(record);
            loadRecords();
            clearForm();
            lblMessage.setText("Record added.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error adding record: " + e.getMessage());
        }
    }

    private void updateRecord() {
        try {
            MaintenanceRecord record = buildRecordFromForm(true);
            if (record == null) return;

            maintenanceService.updateRecord(record);
            loadRecords();
            clearForm();
            lblMessage.setText("Record updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error updating record: " + e.getMessage());
        }
    }

    private void deleteRecord() {
        try {
            String idText = txtId.getText();
            if (idText == null || idText.isBlank()) {
                lblMessage.setText("Select a record to delete.");
                return;
            }

            int id = Integer.parseInt(idText);
            maintenanceService.deleteRecord(id);
            loadRecords();
            clearForm();
            lblMessage.setText("Record deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error deleting record: " + e.getMessage());
        } catch (NumberFormatException e) {
            lblMessage.setText("Invalid ID format.");
        }
    }

    private void clearForm() {
        txtId.clear();
        txtCarId.clear();
        dpDate.setValue(null);
        txtDescription.clear();
        txtCost.clear();
        lblMessage.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    private void fillFormFromSelection(MaintenanceRecord mr) {
        if (mr == null) return;

        txtId.setText(String.valueOf(mr.getMaintenanceId()));
        txtCarId.setText(String.valueOf(mr.getCarId()));
        dpDate.setValue(mr.getMaintenanceDate());
        txtDescription.setText(mr.getDescription());
        txtCost.setText(mr.getCost() != null ? mr.getCost().toString() : "");
    }

    private MaintenanceRecord buildRecordFromForm(boolean requireId) {
        String carText = txtCarId.getText();
        LocalDate date = dpDate.getValue();
        String description = txtDescription.getText();
        String costText = txtCost.getText();

        if (carText == null || carText.isBlank()
                || date == null
                || description == null || description.isBlank()
                || costText == null || costText.isBlank()) {
            lblMessage.setText("Fill all fields.");
            return null;
        }

        Integer id = null;
        if (requireId) {
            String idText = txtId.getText();
            if (idText == null || idText.isBlank()) {
                lblMessage.setText("Select a record first.");
                return null;
            }
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                lblMessage.setText("Invalid ID format.");
                return null;
            }
        }

        int carId;
        try {
            carId = Integer.parseInt(carText);
        } catch (NumberFormatException e) {
            lblMessage.setText("Car ID must be a number.");
            return null;
        }

        BigDecimal cost;
        try {
            cost = new BigDecimal(costText);
        } catch (NumberFormatException e) {
            lblMessage.setText("Cost must be a number.");
            return null;
        }

        MaintenanceRecord mr = new MaintenanceRecord();
        if (id != null) {
            mr.setMaintenanceId(id);
        }
        mr.setCarId(carId);
        mr.setMaintenanceDate(date);
        mr.setDescription(description.trim());
        mr.setCost(cost);

        return mr;
    }
}

