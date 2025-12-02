package com.example.carrentalapp.service;

import com.example.carrentalapp.dao.RequestDAO;
import com.example.carrentalapp.model.Request;

import java.sql.SQLException;
import java.util.List;

// Service for request operations
public class RequestService {

    private final RequestDAO requestDAO = new RequestDAO();

    public List<Request> getAllRequests() throws SQLException {
        return requestDAO.findAll();
    }

    public void deleteRequest(int requestId) throws SQLException {
        requestDAO.delete(requestId);
    }
}
