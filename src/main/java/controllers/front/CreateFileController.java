package controllers.front;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.config.SessionManager;
import tn.esprit.entities.ManagedFile;
import tn.esprit.entities.User;
import tn.esprit.services.ManagedFileServices;

import java.sql.SQLException;

public class CreateFileController {

    @FXML private TextField fileNameField;
    @FXML private TextField filePathField;
    @FXML private TextField fileTypeField;
    @FXML private Label messageLabel;

    private final ManagedFileServices service = new ManagedFileServices();

    @FXML
    private void onSave() {
        String name = fileNameField.getText().trim();
        String path = filePathField.getText().trim();
        String type = fileTypeField.getText().trim();

        if (name.isEmpty() || path.isEmpty() || type.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        User user = SessionManager.getUser();
        if (user == null) {
            showError("User not logged in.");
            return;
        }

        try {
            ManagedFile file = new ManagedFile(
                    user.getId(),
                    name,
                    path,
                    type
            );

            service.ajouter(file);
            showSuccess("File created successfully.");

        } catch (SQLException e) {
            showError("Error: " + e.getMessage());
        }
    }

    @FXML
    private void onClear() {
        fileNameField.clear();
        filePathField.clear();
        fileTypeField.clear();
        messageLabel.setVisible(false);
    }

    private void showError(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setVisible(true);
    }

    private void showSuccess(String msg) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setVisible(true);
    }
}