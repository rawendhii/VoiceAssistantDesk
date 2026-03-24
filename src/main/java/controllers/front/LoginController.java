package controllers.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.config.SessionManager;
import tn.esprit.entities.User;
import tn.esprit.services.UserServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final UserServices userServices = new UserServices();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @FXML
    private void onLogin() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pass = passwordField.getText() == null ? "" : passwordField.getText().trim();

        if (!EMAIL_PATTERN.matcher(email).matches()) { showError("Invalid email."); return; }
        if (pass.length() < 6) { showError("Password must be at least 6 characters."); return; }

        try {
        	String password = pass;
        	User u = userServices.login(email, password);
            if (u == null) {
                showError("Wrong email or password.");
                return;
            }
            SessionManager.setUser(u);

            if ("ADMIN".equalsIgnoreCase(u.getRoleName())) {
                go("/back/BackLayout.fxml", "Back Office");
            } else {
                go("/front/FrontLayout.fxml", "Front Office");
            }

        } catch (SQLException e) {
            showError("DB error: " + e.getMessage());
        }
    }

    @FXML
    private void goRegister() {
        go("/front/Register.fxml", "Register");
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
}