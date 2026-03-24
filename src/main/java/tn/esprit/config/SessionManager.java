package tn.esprit.config;

import tn.esprit.entities.User;

public class SessionManager {

    private static User connectedUser;

    public static void setUser(User user) {
        connectedUser = user;
    }

    public static User getUser() {
        return connectedUser;
    }

    public static void clear() {
        connectedUser = null;
    }

    public static boolean isLoggedIn() {
        return connectedUser != null;
    }
}