module com.example.carrentalapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.carrentalapp to javafx.fxml;
    exports com.example.carrentalapp;
}