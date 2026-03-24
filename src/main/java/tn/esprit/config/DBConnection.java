package tn.esprit.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    private Connection cnx;

    private final String url = System.getProperty("db.url", "jdbc:mysql://127.0.0.1:3307/voice_assistant?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    private final String user = System.getProperty("db.user", "voiceapp");
    private final String password = System.getProperty("db.password", "voiceapp123");
    private DBConnection() {
        try {
            cnx = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to DB!");
        } catch (SQLException e) {
            throw new RuntimeException("DB connection failed: " + e.getMessage(), e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    public Connection getCnx() {
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reconnect to DB: " + e.getMessage(), e);
        }
        return cnx;
    }
}