package com.example.carrentalapp.view;

import com.example.carrentalapp.model.User;
import com.example.carrentalapp.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Dashboard after login
public class DashboardView {

    private final BorderPane root;
    private final User currentUser;
    private final Stage stage;

    private final CarManagementView carManagementView;
    private final CustomerManagementView customerManagementView;
    private final RentalManagementView rentalManagementView;
    private final MaintenanceManagementView maintenanceManagementView;
    private final UserManagementView userManagementView;
    private final ReportsView reportsView;
    private final OnlineRequestsView onlineRequestsView;

    public DashboardView(User currentUser, Stage stage) {
        this.currentUser = currentUser;
        this.stage = stage;

        root = new BorderPane();

        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());

        carManagementView = new CarManagementView();
        customerManagementView = new CustomerManagementView();
        rentalManagementView = new RentalManagementView();
        maintenanceManagementView = new MaintenanceManagementView();
        userManagementView = isAdmin ? new UserManagementView() : null;
        reportsView = new ReportsView(stage);
        onlineRequestsView = new OnlineRequestsView();

        Label welcome = new Label("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");

        HBox topBar = new HBox(welcome);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button btnCars = new Button("Manage Cars");
        Button btnCustomers = new Button("Manage Customers");
        Button btnRentals = new Button("Manage Rentals");
        Button btnMaintenance = new Button("Manage Maintenance");
        Button btnReports = new Button("Reports");
        Button btnRequests = new Button("Online Requests");
        Button btnLogout = new Button("Logout");

        btnCars.setOnAction(e -> root.setCenter(carManagementView.getRoot()));
        btnCustomers.setOnAction(e -> root.setCenter(customerManagementView.getRoot()));
        btnRentals.setOnAction(e -> root.setCenter(rentalManagementView.getRoot()));
        btnMaintenance.setOnAction(e -> root.setCenter(maintenanceManagementView.getRoot()));
        btnReports.setOnAction(e -> root.setCenter(reportsView.getRoot()));
        btnRequests.setOnAction(e -> root.setCenter(onlineRequestsView.getRoot()));
        btnLogout.setOnAction(e -> handleLogout());
        // btnReports to be wired when you add file/CSV exports

        VBox menu = new VBox(10);
        menu.getChildren().addAll(btnCars, btnCustomers, btnRentals);
        if (isAdmin && userManagementView != null) {
            Button btnUsers = new Button("Manage Users");
            btnUsers.setOnAction(e -> root.setCenter(userManagementView.getRoot()));
            menu.getChildren().add(btnUsers);
        }
        menu.getChildren().addAll(btnMaintenance, btnReports, btnRequests, btnLogout);
        menu.setPadding(new Insets(10));
        menu.setAlignment(Pos.TOP_LEFT);

        Label centerLabel = new Label("Select a function from the left menu.");
        BorderPane.setMargin(centerLabel, new Insets(10));

        root.setTop(topBar);
        root.setLeft(menu);
        root.setCenter(centerLabel);
    }

    public Parent getRoot() {
        return root;
    }

    private void handleLogout() {
        Session.clear();
        LoginView loginView = new LoginView(stage);
        Scene scene = new Scene(loginView.getRoot(), 400, 250);
        stage.setTitle("Rent-A-Car - Login");
        stage.setScene(scene);
        stage.setResizable(false);
    }
}
