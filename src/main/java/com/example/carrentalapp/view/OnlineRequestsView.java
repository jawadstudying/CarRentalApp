package com.example.carrentalapp.view;

import com.example.carrentalapp.model.Request;
import com.example.carrentalapp.service.RequestService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Screen for managing online requests (view + delete)
public class OnlineRequestsView {

    private final BorderPane root;
    private final TableView<Request> tableView;
    private final Label lblMessage;

    private final RequestService requestService = new RequestService();

    public OnlineRequestsView() {
        root = new BorderPane();

        Label title = new Label("Online Requests");
        HBox top = new HBox(title);
        top.setPadding(new Insets(10));
        top.setAlignment(Pos.CENTER_LEFT);

        tableView = new TableView<>();
        setupTableColumns();

        Button btnLoad = new Button("Load Requests");
        Button btnDelete = new Button("Delete Selected");

        btnLoad.setOnAction(e -> loadRequests());
        btnDelete.setOnAction(e -> deleteSelected());

        lblMessage = new Label();

        HBox bottom = new HBox(10, btnLoad, btnDelete, lblMessage);
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(10));

        root.setTop(top);
        root.setCenter(tableView);
        root.setBottom(bottom);

        loadRequests();
    }

    public Parent getRoot() {
        return root;
    }

    private void setupTableColumns() {
        TableColumn<Request, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("requestId"));

        TableColumn<Request, String> colFirst = new TableColumn<>("First Name");
        colFirst.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Request, String> colLast = new TableColumn<>("Last Name");
        colLast.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Request, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Request, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Request, Integer> colCar = new TableColumn<>("Car ID");
        colCar.setCellValueFactory(new PropertyValueFactory<>("carId"));

        TableColumn<Request, LocalDateTime> colCreated = new TableColumn<>("Created At");
        colCreated.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colCreated.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(fmt.format(item));
                }
            }
        });

        tableView.getColumns().addAll(colId, colFirst, colLast, colPhone, colEmail, colCar, colCreated);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadRequests() {
        try {
            List<Request> requests = requestService.getAllRequests();
            ObservableList<Request> data = FXCollections.observableArrayList(requests);
            tableView.setItems(data);
            lblMessage.setText("Requests loaded: " + requests.size());
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error loading requests: " + e.getMessage());
        }
    }

    private void deleteSelected() {
        Request selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblMessage.setText("Select a request to delete.");
            return;
        }

        try {
            requestService.deleteRequest(selected.getRequestId());
            loadRequests();
            lblMessage.setText("Request deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Error deleting request: " + e.getMessage());
        }
    }
}
