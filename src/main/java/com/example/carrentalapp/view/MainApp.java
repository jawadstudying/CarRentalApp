package com.example.carrentalapp.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Application entry point
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView(primaryStage);

        Scene scene = new Scene(loginView.getRoot(), 400, 250);
        primaryStage.setTitle("Rent-A-Car - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
