package com.example.carrentalapp.view;

import com.example.carrentalapp.model.User;
import com.example.carrentalapp.service.AuthService;
import com.example.carrentalapp.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Login screen
public class LoginView {

    private final VBox root;
    private final TextField txtUsername;
    private final PasswordField txtPassword;
    private final Label lblMessage;

    private final AuthService authService = new AuthService();
    private final Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;

        txtUsername = new TextField();
        txtPassword = new PasswordField();
        lblMessage = new Label();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Username:"), 0, 0);
        form.add(txtUsername, 1, 0);
        form.add(new Label("Password:"), 0, 1);
        form.add(txtPassword, 1, 1);

        Button btnLogin = new Button("Login");
        btnLogin.setOnAction(e -> handleLogin());

        root = new VBox(10, form, btnLogin, lblMessage);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {
            User user = authService.login(username, password);
            if (user == null) {
                lblMessage.setText("Invalid username or password.");
            } else {
                Session.setCurrentUser(user);
                openDashboard(user);
            }
        } catch (IllegalArgumentException ex) {
            lblMessage.setText(ex.getMessage());
        } catch (Exception ex) {
            lblMessage.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void openDashboard(User user) {
        DashboardView dashboardView = new DashboardView(user, stage);
        Scene scene = new Scene(dashboardView.getRoot(), 800, 600);
        stage.setTitle("Rent-A-Car - Dashboard");
        stage.setScene(scene);
        stage.setResizable(true);
    }
}
