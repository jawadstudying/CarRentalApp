package com.example.carrentalapp.view;

import com.example.carrentalapp.model.User;
import com.example.carrentalapp.service.UserService;
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

// Screen for managing users (employees)
public class UserManagementView {

    private final BorderPane root;

    private final TableView<User> tableView;
    private final TextField txtId;
    private final TextField txtUsername;
    private final TextField txtPassword;
    private final ComboBox<String> cbRole;
    private final TextField txtSalary;
    private final Label lblMessage;

    private final UserService userService = new UserService();

    public UserManagementView() {
        root = new BorderPane();

        Label title = new Label("User Management");
        HBox top = new HBox(title);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);

        tableView = new TableView<>();
        setupTableColumns();

        txtId = new TextField();
        txtId.setPromptText("ID");
        txtId.setEditable(false);

        txtUsername = new TextField();
        txtUsername.setPromptText("Username");

        txtPassword = new TextField();
        txtPassword.setPromptText("Password");

        cbRole = new ComboBox<>();
        cbRole.getItems().addAll("ADMIN", "EMPLOYEE");
        cbRole.setPromptText("Role");

        txtSalary = new TextField();
        txtSalary.setPromptText("Salary");

        lblMessage = new Label();

        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("ID:"), 0, 0);
        form.add(txtId, 1, 0);

        form.add(new Label("Username:"), 0, 1);
        form.add(txtUsername, 1, 1);

        form.add(new Label("Password:"), 2, 1);
        form.add(txtPassword, 3, 1);

        form.add(new Label("Role:"), 0, 2);
        form.add(cbRole, 1, 2);

        form.add(new Label("Salary:"), 2, 2);
        form.add(txtSalary, 3, 2);

        Button btnLoad = new Button("Load Users");
        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnClear = new Button("Clear");

        btnLoad.setOnAction(e -> loadUsers());
        btnAdd.setOnAction(e -> addUser());
        btnUpdate.setOnAction(e -> updateUser());
        btnDelete.setOnAction(e -> deleteUser());
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

        loadUsers();
    }

    public Parent getRoot() {
        return root;
    }

    private void setupTableColumns() {
        TableColumn<User, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> colUsername = new TableColumn<>("Username");
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, BigDecimal> colSalary = new TableColumn<>("Salary");
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        tableView.getColumns().addAll(colId, colUsername, colRole, colSalary);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadUsers() {
        try {
            List<User> users = userService.getAllUsers();
            ObservableList<User> data = FXCollections.observableArrayList(users);
            tableView.setItems(data);
            lblMessage.setText("Users loaded: " + users.size());
        } catch (SecurityException se) {
            lblMessage.setText(se.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error loading users: " + e.getMessage());
        }
    }

    private void addUser() {
        try {
            User user = buildUserFromForm(false);
            if (user == null) return;

            userService.addUser(user);
            loadUsers();
            clearForm();
            lblMessage.setText("User added.");
        } catch (SecurityException se) {
            lblMessage.setText(se.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error adding user: " + e.getMessage());
        }
    }

    private void updateUser() {
        try {
            User user = buildUserFromForm(true);
            if (user == null) return;

            userService.updateUser(user);
            loadUsers();
            clearForm();
            lblMessage.setText("User updated.");
        } catch (SecurityException se) {
            lblMessage.setText(se.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error updating user: " + e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            String idText = txtId.getText();
            if (idText == null || idText.isBlank()) {
                lblMessage.setText("Select a user to delete.");
                return;
            }

            int id = Integer.parseInt(idText);
            userService.deleteUser(id);
            loadUsers();
            clearForm();
            lblMessage.setText("User deleted.");
        } catch (SecurityException se) {
            lblMessage.setText(se.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error deleting user: " + e.getMessage());
        } catch (NumberFormatException e) {
            lblMessage.setText("Invalid ID format.");
        }
    }

    private void clearForm() {
        txtId.clear();
        txtUsername.clear();
        txtPassword.clear();
        cbRole.setValue(null);
        txtSalary.clear();
        lblMessage.setText("");
        tableView.getSelectionModel().clearSelection();
    }

    private void fillFormFromSelection(User u) {
        if (u == null) return;

        txtId.setText(String.valueOf(u.getUserId()));
        txtUsername.setText(u.getUsername());
        txtPassword.setText(u.getPasswordHash());
        cbRole.setValue(u.getRole());
        txtSalary.setText(u.getSalary() != null ? u.getSalary().toString() : "");
    }

    private User buildUserFromForm(boolean requireId) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String role = cbRole.getValue();
        String salaryText = txtSalary.getText();

        if (username == null || username.isBlank()
                || password == null || password.isBlank()
                || role == null || role.isBlank()
                || salaryText == null || salaryText.isBlank()) {
            lblMessage.setText("Fill all fields.");
            return null;
        }

        Integer id = null;
        if (requireId) {
            String idText = txtId.getText();
            if (idText == null || idText.isBlank()) {
                lblMessage.setText("Select a user first.");
                return null;
            }
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                lblMessage.setText("Invalid ID format.");
                return null;
            }
        }

        BigDecimal salary;
        try {
            salary = new BigDecimal(salaryText);
        } catch (NumberFormatException e) {
            lblMessage.setText("Salary must be a number.");
            return null;
        }

        User u = new User();
        if (id != null) {
            u.setUserId(id);
        }
        u.setUsername(username.trim());
        u.setPasswordHash(password.trim()); // simple for project
        u.setRole(role.trim());
        u.setSalary(salary);

        return u;
    }
}
