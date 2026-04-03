package controllers.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tn.esprit.config.SessionManager;
import tn.esprit.entities.CommandHistory;
import tn.esprit.entities.User;
import tn.esprit.entities.VoiceCommand;
import tn.esprit.services.CommandHistoryServices;
import tn.esprit.services.VoiceCommandServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class VoiceController {

    @FXML private TextArea recognizedTextArea;
    @FXML private Label messageLabel;

    private final VoiceCommandServices voiceCommandServices = new VoiceCommandServices();
    private final CommandHistoryServices commandHistoryServices = new CommandHistoryServices();

    @FXML
    private void onStartListening() {
        recognizedTextArea.setText("create file");
        showSuccess("Listening started. Simulated command inserted.");
    }

    @FXML
    private void onStopListening() {
        showSuccess("Listening stopped.");
    }

    @FXML
    private void onClear() {
        recognizedTextArea.clear();
        hideMessage();
    }

    @FXML
    private void onExecuteCommand() {
        String text = recognizedTextArea.getText() == null ? "" : recognizedTextArea.getText().trim();

        if (text.isEmpty()) {
            showError("Please enter or simulate a command first.");
            return;
        }

        User currentUser = SessionManager.getUser();
        if (currentUser == null) {
            showError("No user is currently logged in.");
            return;
        }

        try {
            List<VoiceCommand> commands = voiceCommandServices.afficher();

            VoiceCommand matchedCommand = null;
            for (VoiceCommand command : commands) {
                if (command.isActive()
                        && command.getKeyword() != null
                        && text.equalsIgnoreCase(command.getKeyword().trim())) {
                    matchedCommand = command;
                    break;
                }
            }

            if (matchedCommand == null) {
                showError("Unknown or inactive command.");
                return;
            }

            boolean executed = executeAction(matchedCommand.getKeyword());

            if (executed) {
                saveHistory(currentUser.getId(), matchedCommand.getId(), text, "SUCCESS");
                showSuccess("Command executed successfully: " + matchedCommand.getLabel());
            } else {
                saveHistory(currentUser.getId(), matchedCommand.getId(), text, "FAILED");
                showError("Command recognized but action failed.");
            }

        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private boolean executeAction(String keyword) {
        String k = keyword == null ? "" : keyword.trim().toLowerCase();

        switch (k) {
            case "create file":
                return loadView("/front/CreateFile.fxml");

            case "read file":
                return loadView("/front/ReadFile.fxml");

            case "delete file":
                return loadView("/front/DeleteFile.fxml");

            case "open profile":
                return loadView("/front/Profile.fxml");

            case "logout":
                return logoutAndGoToLogin();

            default:
                return false;
        }
    }

    private boolean loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node node = loader.load();

            StackPane contentArea = (StackPane) recognizedTextArea.getScene().lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(node);
                return true;
            }

            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean logoutAndGoToLogin() {
        try {
            SessionManager.clear();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) recognizedTextArea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void saveHistory(long userId, int commandId, String executedText, String status) {
        try {
            CommandHistory history = new CommandHistory();
            history.setUserId(userId);
            history.setCommandId(commandId);
            history.setExecutedText(executedText);
            history.setStatus(status);
            commandHistoryServices.ajouter(history);
        } catch (SQLException ignored) {
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

    private void hideMessage() {
        messageLabel.setVisible(false);
    }
}