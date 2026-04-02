package controllers.front;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.config.SessionManager;
import tn.esprit.entities.ManagedFile;
import tn.esprit.entities.User;
import tn.esprit.models.ManagedFileRow;
import tn.esprit.services.ManagedFileServices;

import java.sql.SQLException;
import java.util.List;

public class UserManagedFilesController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeFilterCombo;
    @FXML private TextField idField;
    @FXML private TextField fileNameField;
    @FXML private TextField filePathField;
    @FXML private TextField fileTypeField;
    @FXML private Label messageLabel;

    @FXML private TableView<ManagedFileRow> filesTable;
    @FXML private TableColumn<ManagedFileRow, Integer> colId;
    @FXML private TableColumn<ManagedFileRow, String> colFileName;
    @FXML private TableColumn<ManagedFileRow, String> colFilePath;
    @FXML private TableColumn<ManagedFileRow, String> colFileType;
    @FXML private TableColumn<ManagedFileRow, String> colCreatedAt;
    @FXML private TableColumn<ManagedFileRow, String> colUpdatedAt;

    private final ManagedFileServices managedFileServices = new ManagedFileServices();
    private final ObservableList<ManagedFileRow> data = FXCollections.observableArrayList();

    private long currentUserId;

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getUser();
        if (currentUser == null) {
            showError("No connected user found.");
            return;
        }

        currentUserId = currentUser.getId();

        colId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colFileName.setCellValueFactory(c -> c.getValue().fileNameProperty());
        colFilePath.setCellValueFactory(c -> c.getValue().filePathProperty());
        colFileType.setCellValueFactory(c -> c.getValue().fileTypeProperty());
        colCreatedAt.setCellValueFactory(c -> c.getValue().createdAtProperty());
        colUpdatedAt.setCellValueFactory(c -> c.getValue().updatedAtProperty());

        filesTable.setItems(data);

        typeFilterCombo.setItems(FXCollections.observableArrayList("ALL", "txt", "pdf", "docx"));
        typeFilterCombo.setValue("ALL");

        loadFiles(null, "ALL");
    }

    private void loadFiles(String keyword, String typeFilter) {
        data.clear();
        try {
            List<ManagedFileRow> rows = managedFileServices.fetchRowsByUserId(currentUserId, keyword, typeFilter);
            data.addAll(rows);
            hideMessage();
        } catch (SQLException e) {
            showError("Error loading your files: " + e.getMessage());
        }
    }

    private String validateForm(boolean isAdd) {
        String fileName = fileNameField.getText() == null ? "" : fileNameField.getText().trim();
        String filePath = filePathField.getText() == null ? "" : filePathField.getText().trim();
        String fileType = fileTypeField.getText() == null ? "" : fileTypeField.getText().trim();

        if (fileName.length() < 3) {
            return "File name must be at least 3 characters.";
        }

        if (filePath.length() < 5) {
            return "File path must be at least 5 characters.";
        }

        if (fileType.length() < 2) {
            return "File type must be at least 2 characters.";
        }

        if (isAdd) {
            try {
                if (managedFileServices.filePathExists(filePath)) {
                    return "This file path already exists.";
                }
            } catch (SQLException e) {
                return "Validation error: " + e.getMessage();
            }
        }

        return null;
    }

    @FXML
    private void onSearch() {
        loadFiles(searchField.getText(), typeFilterCombo.getValue());
    }

    @FXML
    private void onRefresh() {
        searchField.clear();
        typeFilterCombo.setValue("ALL");
        loadFiles(null, "ALL");
        onClear();
    }

    @FXML
    private void onLoadSelected() {
        ManagedFileRow row = filesTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Please select a file.");
            return;
        }

        idField.setText(String.valueOf(row.getId()));
        fileNameField.setText(row.getFileName());
        filePathField.setText(row.getFilePath());
        fileTypeField.setText(row.getFileType());
        hideMessage();
    }

    @FXML
    private void onAdd() {
        String err = validateForm(true);
        if (err != null) {
            showError(err);
            return;
        }

        try {
            ManagedFile file = new ManagedFile(
                    currentUserId,
                    fileNameField.getText().trim(),
                    filePathField.getText().trim(),
                    fileTypeField.getText().trim()
            );

            managedFileServices.ajouter(file);
            loadFiles(null, "ALL");
            onClear();
            showSuccess("File added successfully.");

        } catch (SQLException e) {
            showError("Add failed: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        if (idField.getText() == null || idField.getText().trim().isEmpty()) {
            showError("Load a file from the table before updating.");
            return;
        }

        String err = validateForm(false);
        if (err != null) {
            showError(err);
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());
        String filePath = filePathField.getText().trim();

        try {
            if (managedFileServices.filePathExistsForOtherFile(filePath, id)) {
                showError("This file path already exists.");
                return;
            }

            ManagedFile file = new ManagedFile(
                    currentUserId,
                    fileNameField.getText().trim(),
                    filePath,
                    fileTypeField.getText().trim()
            );
            file.setId(id);

            managedFileServices.modifier(file);
            loadFiles(null, "ALL");
            onClear();
            showSuccess("File updated successfully.");

        } catch (SQLException e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        ManagedFileRow row = filesTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Please select a file to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete file?");
        alert.setContentText(row.getFileName());

        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            managedFileServices.supprimer(row.getId());
            loadFiles(searchField.getText(), typeFilterCombo.getValue());
            onClear();
            showSuccess("File deleted successfully.");
        } catch (SQLException e) {
            showError("Delete failed: " + e.getMessage());
        }
    }

    @FXML
    private void onClear() {
        idField.clear();
        fileNameField.clear();
        filePathField.clear();
        fileTypeField.clear();
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