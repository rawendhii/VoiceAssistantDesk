package controllers.back;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.VoiceCommand;
import tn.esprit.models.VoiceCommandRow;
import tn.esprit.services.VoiceCommandServices;

import java.sql.SQLException;
import java.util.List;

public class VoiceCommandsListController {

    @FXML private TextField searchField;
    @FXML private TextField idField;
    @FXML private TextField labelField;
    @FXML private TextField keywordField;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox activeCheckBox;
    @FXML private Label messageLabel;

    @FXML private TableView<VoiceCommandRow> commandsTable;
    @FXML private TableColumn<VoiceCommandRow, Integer> colId;
    @FXML private TableColumn<VoiceCommandRow, String> colLabel;
    @FXML private TableColumn<VoiceCommandRow, String> colKeyword;
    @FXML private TableColumn<VoiceCommandRow, String> colDescription;
    @FXML private TableColumn<VoiceCommandRow, Boolean> colActive;

    private final VoiceCommandServices voiceCommandServices = new VoiceCommandServices();
    private final ObservableList<VoiceCommandRow> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colLabel.setCellValueFactory(c -> c.getValue().labelProperty());
        colKeyword.setCellValueFactory(c -> c.getValue().keywordProperty());
        colDescription.setCellValueFactory(c -> c.getValue().descriptionProperty());
        colActive.setCellValueFactory(c -> c.getValue().activeProperty());

        commandsTable.setItems(data);
        activeCheckBox.setSelected(true);

        loadCommands(null);
    }

    private void loadCommands(String keyword) {
        data.clear();
        try {
            List<VoiceCommandRow> rows = voiceCommandServices.fetchRows(keyword);
            data.addAll(rows);
            hideMessage();
        } catch (SQLException e) {
            showError("Error loading voice commands: " + e.getMessage());
        }
    }

    private String validateForm(boolean isAdd) {
        String label = labelField.getText() == null ? "" : labelField.getText().trim();
        String keyword = keywordField.getText() == null ? "" : keywordField.getText().trim();
        String description = descriptionArea.getText() == null ? "" : descriptionArea.getText().trim();

        if (label.length() < 3) {
            return "Label must be at least 3 characters.";
        }

        if (keyword.length() < 3) {
            return "Keyword must be at least 3 characters.";
        }

        if (description.length() > 255) {
            return "Description must not exceed 255 characters.";
        }

        if (isAdd) {
            try {
                if (voiceCommandServices.keywordExists(keyword)) {
                    return "This keyword already exists.";
                }
            } catch (SQLException e) {
                return "Validation error: " + e.getMessage();
            }
        }

        return null;
    }

    @FXML
    private void onSearch() {
        loadCommands(searchField.getText());
    }

    @FXML
    private void onRefresh() {
        searchField.clear();
        loadCommands(null);
        onClear();
    }

    @FXML
    private void onLoadSelected() {
        VoiceCommandRow row = commandsTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Please select a voice command.");
            return;
        }

        idField.setText(String.valueOf(row.getId()));
        labelField.setText(row.getLabel());
        keywordField.setText(row.getKeyword());
        descriptionArea.setText(row.getDescription());
        activeCheckBox.setSelected(row.isActive());
        hideMessage();
    }

    @FXML
    private void onAdd() {
        String err = validateForm(true);
        if (err != null) {
            showError(err);
            return;
        }

        VoiceCommand command = new VoiceCommand(
                labelField.getText().trim(),
                keywordField.getText().trim(),
                descriptionArea.getText() == null ? "" : descriptionArea.getText().trim(),
                activeCheckBox.isSelected()
        );

        try {
            voiceCommandServices.ajouter(command);
            loadCommands(null);
            onClear();
            showSuccess("Voice command added successfully.");
        } catch (SQLException e) {
            showError("Add failed: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        if (idField.getText() == null || idField.getText().trim().isEmpty()) {
            showError("Load a voice command from the table before updating.");
            return;
        }

        String err = validateForm(false);
        if (err != null) {
            showError(err);
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());
        String keyword = keywordField.getText().trim();

        try {
            if (voiceCommandServices.keywordExistsForOtherCommand(keyword, id)) {
                showError("This keyword already exists.");
                return;
            }

            VoiceCommand command = new VoiceCommand(
                    id,
                    labelField.getText().trim(),
                    keyword,
                    descriptionArea.getText() == null ? "" : descriptionArea.getText().trim(),
                    activeCheckBox.isSelected()
            );

            voiceCommandServices.modifier(command);
            loadCommands(null);
            onClear();
            showSuccess("Voice command updated successfully.");

        } catch (SQLException e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        VoiceCommandRow row = commandsTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Please select a voice command to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete voice command?");
        alert.setContentText(row.getLabel() + " (" + row.getKeyword() + ")");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            voiceCommandServices.supprimer(row.getId());
            loadCommands(null);
            onClear();
            showSuccess("Voice command deleted successfully.");
        } catch (SQLException e) {
            showError("Delete failed: " + e.getMessage());
        }
    }

    @FXML
    private void onClear() {
        idField.clear();
        labelField.clear();
        keywordField.clear();
        descriptionArea.clear();
        activeCheckBox.setSelected(true);
        hideMessage();
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