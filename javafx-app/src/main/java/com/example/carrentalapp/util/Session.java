package com.example.carrentalapp.util;

import com.example.carrentalapp.model.User;

// Holds data about the currently logged-in user
public class Session {

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void clear() {
        currentUser = null;
    }
}
