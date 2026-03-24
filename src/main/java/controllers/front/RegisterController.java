package controllers.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.services.UserServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final UserServices userServices = new UserServices();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @FXML
    private void onRegister() {
        String full = fullNameField.getText() == null ? "" : fullNameField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pass = passwordField.getText() == null ? "" : passwordField.getText().trim();

        if (full.length() < 3) { showError("Full name must be at least 3 characters."); return; }
        if (!EMAIL_PATTERN.matcher(email).matches()) { showError("Invalid email."); return; }
        if (pass.length() < 6) { showError("Password must be at least 6 characters."); return; }

        try {
            if (userServices.emailExists(email)) {
                showError("This email already exists.");
                return;
            }

            int userRoleId = userServices.getRoleIdByName("USER");
            if (userRoleId == -1) {
                showError("Role USER not found. Insert roles first.");
                return;
            }

            String password = pass;
            boolean ok = userServices.registerUser(full, email, password, userRoleId);
            if (ok) {
                showSuccess("Account created. You can login now.");
            } else {
                showError("Register failed.");
            }

        } catch (SQLException e) {
            showError("DB error: " + e.getMessage());
        }
    }

    @FXML
    private void goLogin() {
        go("/front/Login.fxml", "Login");
    }

    private void go(String fxml, String title) {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(fxml)));
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            showError("FXML not found: " + fxml);
        }
    }

    private void showError(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: #b91c1c;");
        messageLabel.setVisible(true);
    }

    private void showSuccess(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: #15803d;");
        messageLabel.setVisible(true);
    }
}