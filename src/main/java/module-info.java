module com.example.carrentalapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.carrentalapp to javafx.fxml;
    opens com.example.carrentalapp.model to javafx.base;
    exports com.example.carrentalapp.view;
    exports com.example.carrentalapp;
}