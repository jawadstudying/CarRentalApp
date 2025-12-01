package com.example.carrentalapp.view;

import com.example.carrentalapp.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

    public DashboardView(User currentUser, Stage stage) {
        this.currentUser = currentUser;
        this.stage = stage;

        root = new BorderPane();
        carManagementView = new CarManagementView();

        Label welcome = new Label("Welcome, " + currentUser.getUsername());

        HBox topBar = new HBox(welcome);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button btnCars = new Button("Manage Cars");
        Button btnCustomers = new Button("Manage Customers");
        Button btnRentals = new Button("Manage Rentals");
        Button btnUsers = new Button("Manage Users");
        Button btnReports = new Button("Reports");

        btnCars.setOnAction(e -> root.setCenter(carManagementView.getRoot()));
        // other buttons will be wired later

        VBox menu = new VBox(10, btnCars, btnCustomers, btnRentals, btnUsers, btnReports);
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
}
