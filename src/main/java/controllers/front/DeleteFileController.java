package controllers.front;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.services.ManagedFileServices;

import java.sql.SQLException;

public class DeleteFileController {

    @FXML private TextField idField;
    @FXML private Label messageLabel;

    private final ManagedFileServices service = new ManagedFileServices();

    @FXML
    private void onDelete() {
        try {
            int id = Integer.parseInt(idField.getText());
            service.supprimer(id);
            showSuccess("File deleted successfully.");
        } catch (NumberFormatException e) {
            showError("Invalid ID.");
        } catch (SQLException e) {
            showError("Error: " + e.getMessage());
        }
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