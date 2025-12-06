package com.example.carrentalapp.view;

import com.example.carrentalapp.model.Customer;
import com.example.carrentalapp.service.CustomerService;
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

import java.sql.SQLException;
import java.util.List;

// Screen for managing customers
public class CustomerManagementView {

    private final BorderPane root;

    private final TableView<Customer> tableView;
    private final TextField txtId;
    private final TextField txtFirstName;
    private final TextField txtLastName;
    private final TextField txtPhone;
    private final TextField txtEmail;
    private final Label lblMessage;

    private final CustomerService customerService = new CustomerService();

    public CustomerManagementView() {
        root = new BorderPane();

        Label title = new Label("Customer Management");
        HBox top = new HBox(title);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);

        tableView = new TableView<>();
        setupTableColumns();

        txtId = new TextField();
        txtId.setPromptText("ID");
        txtId.setEditable(false);

        txtFirstName = new TextField();
        txtFirstName.setPromptText("First Name");

        txtLastName = new TextField();
        txtLastName.setPromptText("Last Name");

        txtPhone = new TextField();
        txtPhone.setPromptText("Phone");

        txtEmail = new TextField();
        txtEmail.setPromptText("Email");

        lblMessage = new Label();

        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("ID:"), 0, 0);
        form.add(txtId, 1, 0);

        form.add(new Label("First Name:"), 0, 1);
        form.add(txtFirstName, 1, 1);

        form.add(new Label("Last Name:"), 2, 1);
        form.add(txtLastName, 3, 1);

        form.add(new Label("Phone:"), 0, 2);
        form.add(txtPhone, 1, 2);

        form.add(new Label("Email:"), 2, 2);
        form.add(txtEmail, 3, 2);

        Button btnLoad = new Button("Load Customers");
        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnClear = new Button("Clear");

        btnLoad.setOnAction(e -> loadCustomers());
        btnAdd.setOnAction(e -> addCustomer());
        btnUpdate.setOnAction(e -> updateCustomer());
        btnDelete.setOnAction(e -> deleteCustomer());
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

        loadCustomers();
    }

    public Parent getRoot() {
        return root;
    }

    private void setupTableColumns() {
        TableColumn<Customer, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        TableColumn<Customer, String> colFirst = new TableColumn<>("First Name");
        colFirst.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Customer, String> colLast = new TableColumn<>("Last Name");
        colLast.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Customer, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Customer, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tableView.getColumns().addAll(colId, colFirst, colLast, colPhone, colEmail);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            ObservableList<Customer> data = FXCollections.observableArrayList(customers);
            tableView.setItems(data);
            lblMessage.setText("Customers loaded: " + customers.size());
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error loading customers: " + e.getMessage());
        }
    }

    private void addCustomer() {
        try {
            Customer c = buildCustomerFromForm(false);
            if (c == null) return;

            customerService.addCustomer(c);
            loadCustomers();
            clearForm();
            lblMessage.setText("Customer added.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error adding customer: " + e.getMessage());
        }
    }

    private void updateCustomer() {
        try {
            Customer c = buildCustomerFromForm(true);
            if (c == null) return;

            customerService.updateCustomer(c);
            loadCustomers();
            clearForm();
            lblMessage.setText("Customer updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error updating customer: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        try {
            String idText = txtId.getText();
            if (idText == null || idText.isBlank()) {
                lblMessage.setText("Select a customer to delete.");
                return;
            }

            int id = Integer.parseInt(idText);
            customerService.deleteCustomer(id);
            loadCustomers();
            clearForm();
            lblMessage.setText("Customer deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error deleting customer: " + e.getMessage());
        } catch (NumberFormatException e) {
            lblMessage.setText("Invalid ID format.");
        }
    }

    private void clearForm() {
        txtId.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtPhone.clear();
        txtEmail.clear();
        lblMessage.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    private void fillFormFromSelection(Customer c) {
        if (c == null) return;

        txtId.setText(String.valueOf(c.getCustomerId()));
        txtFirstName.setText(c.getFirstName());
        txtLastName.setText(c.getLastName());
        txtPhone.setText(c.getPhone());
        txtEmail.setText(c.getEmail());
    }

    private Customer buildCustomerFromForm(boolean requireId) {
        String first = txtFirstName.getText();
        String last = txtLastName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        if (first == null || first.isBlank()
                || last == null || last.isBlank()
                || phone == null || phone.isBlank()
                || email == null || email.isBlank()) {
            lblMessage.setText("Fill all fields.");
            return null;
        }

        Integer id = null;
        if (requireId) {
            String idText = txtId.getText();
            if (idText == null || idText.isBlank()) {
                lblMessage.setText("Select a customer first.");
                return null;
            }
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                lblMessage.setText("Invalid ID format.");
                return null;
            }
        }

        Customer c = new Customer();
        if (id != null) {
            c.setCustomerId(id);
        }
        c.setFirstName(first.trim());
        c.setLastName(last.trim());
        c.setPhone(phone.trim());
        c.setEmail(email.trim());

        return c;
    }
}
