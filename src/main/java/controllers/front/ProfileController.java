package controllers.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import tn.esprit.config.SessionManager;
import tn.esprit.entities.User;
import tn.esprit.services.UserServices;

import java.sql.SQLException;

public class ProfileController {

    // Personal Information Fields
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private Label memberSinceLabel;
    @FXML private Label messageLabel;
    
    // Password Fields
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label passwordMessageLabel;
    @FXML private Label confirmPasswordValidationLabel;

    private UserServices userServices;
    private User currentUser;

    @FXML
    public void initialize() {
        userServices = new UserServices();
        loadUserProfile();
        setupRealTimeValidation();
    }

    private void setupRealTimeValidation() {
        newPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateConfirmPassword());
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateConfirmPassword());
    }

    private void validateConfirmPassword() {
        String newPwd = newPasswordField.getText();
        String confirmPwd = confirmPasswordField.getText();
        
        if (newPwd.isEmpty() || confirmPwd.isEmpty()) {
            confirmPasswordValidationLabel.setVisible(false);
            return;
        }
        
        if (newPwd.equals(confirmPwd)) {
            confirmPasswordValidationLabel.setText("✓ Passwords match");
            confirmPasswordValidationLabel.setStyle("-fx-text-fill: green;");
            confirmPasswordValidationLabel.setVisible(true);
        } else {
            confirmPasswordValidationLabel.setText("✗ Passwords do not match");
            confirmPasswordValidationLabel.setStyle("-fx-text-fill: red;");
            confirmPasswordValidationLabel.setVisible(true);
        }
    }

    private void loadUserProfile() {
        currentUser = SessionManager.getUser();
        
        if (currentUser == null) {
            showError("No user logged in");
            return;
        }

        try {
            User fullUser = userServices.getUserById(currentUser.getId());
            
            if (fullUser != null) {
                fullNameField.setText(fullUser.getFullName());
                emailField.setText(fullUser.getEmail());
                memberSinceLabel.setText("Member");
            } else {
                showError("Unable to load profile");
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        if (!validateFields()) {
            return;
        }

        String newFullName = fullNameField.getText().trim();
        String newEmail = emailField.getText().trim();

        try {
            if (userServices.emailExistsForOtherUser(newEmail, currentUser.getId())) {
                showError("This email is already used by another user");
                return;
            }

            currentUser.setFullName(newFullName);
            currentUser.setEmail(newEmail);

            if (userServices.updateProfile(currentUser)) {
                SessionManager.setUser(currentUser);
                showSuccess("Profile updated successfully!");
                loadUserProfile();
            } else {
                showError("Error updating profile");
            }
        } catch (SQLException e) {
            showError("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleChangePassword() {
        String currentPwd = currentPasswordField.getText();
        String newPwd = newPasswordField.getText();
        String confirmPwd = confirmPasswordField.getText();

        // Validation
        String validationError = validatePasswordFieldsWithDetails(currentPwd, newPwd, confirmPwd);
        if (validationError != null) {
            showPasswordMessage(validationError, "error");
            return;
        }

        try {
            if (userServices.changePassword(currentUser.getId(), currentPwd, newPwd)) {
                showPasswordMessage("✓ Password changed successfully!", "success");
                clearPasswordFields();
                confirmPasswordValidationLabel.setVisible(false);
            } else {
                showPasswordMessage("✗ Incorrect current password", "error");
            }
        } catch (SQLException e) {
            showPasswordMessage("✗ Error: " + e.getMessage(), "error");
        }
    }

    private void showPasswordMessage(String message, String type) {
        if ("success".equals(type)) {
            // Afficher une popup de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } else {
            // Afficher une popup d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account Confirmation");
        alert.setHeaderText("Delete your account?");
        alert.setContentText("This action is irreversible. Are you sure you want to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                userServices.supprimer(currentUser.getId());
                SessionManager.clear();
                redirectToLogin();
            } catch (SQLException e) {
                showError("Error deleting account: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/FrontHome.fxml"));
            Node homeView = loader.load();
            
            StackPane contentArea = (StackPane) fullNameField.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(homeView);
            } else {
                FXMLLoader layoutLoader = new FXMLLoader(getClass().getResource("/front/FrontLayout.fxml"));
                Parent layoutView = layoutLoader.load();
                fullNameField.getScene().setRoot(layoutView);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error returning to home");
        }
    }

    @FXML
    private void handleCancel() {
        loadUserProfile();
        messageLabel.setText("");
    }

    @FXML
    private void clearPasswordFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
        passwordMessageLabel.setText("");
        confirmPasswordValidationLabel.setVisible(false);
    }

    private boolean validateFields() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();

        if (fullName.isEmpty()) {
            showError("Full name cannot be empty");
            return false;
        }
        if (fullName.length() < 3) {
            showError("Full name must be at least 3 characters");
            return false;
        }
        if (email.isEmpty()) {
            showError("Email cannot be empty");
            return false;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showError("Invalid email address");
            return false;
        }
        return true;
    }

    private String validatePasswordFieldsWithDetails(String currentPwd, String newPwd, String confirmPwd) {
        if (currentPwd.isEmpty()) {
            return "Please enter your current password";
        }
        if (newPwd.isEmpty()) {
            return "Please enter a new password";
        }
        if (newPwd.length() < 6) {
            return "Password must be at least 6 characters";
        }
        if (!newPwd.equals(confirmPwd)) {
            return "Passwords do not match";
        }
        if (currentPwd.equals(newPwd)) {
            return "New password must be different from current";
        }
        return null;
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setVisible(true);
        
        new Thread(() -> {
            try { Thread.sleep(3000); } catch (InterruptedException e) {}
            javafx.application.Platform.runLater(() -> messageLabel.setVisible(false));
        }).start();
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setVisible(true);
        
        new Thread(() -> {
            try { Thread.sleep(3000); } catch (InterruptedException e) {}
            javafx.application.Platform.runLater(() -> messageLabel.setVisible(false));
        }).start();
    }

    private void redirectToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Login.fxml"));
            Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) fullNameField.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Assistant Vocal - Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}