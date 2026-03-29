package controllers.back;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.ManagedFile;
import tn.esprit.models.ManagedFileRow;
import tn.esprit.services.ManagedFileServices;
import tn.esprit.services.UserServices;

import java.sql.SQLException;
import java.util.List;

public class ManagedFilesController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeFilterCombo;
    @FXML private TextField idField;
    @FXML private TextField userIdField;
    @FXML private TextField fileNameField;
    @FXML private TextField filePathField;
    @FXML private TextField fileTypeField;
    @FXML private Label messageLabel;

    @FXML private TableView<ManagedFileRow> filesTable;
    @FXML private TableColumn<ManagedFileRow, Integer> colId;
    @FXML private TableColumn<ManagedFileRow, String> colUser;
    @FXML private TableColumn<ManagedFileRow, String> colFileName;
    @FXML private TableColumn<ManagedFileRow, String> colFilePath;
    @FXML private TableColumn<ManagedFileRow, String> colFileType;
    @FXML private TableColumn<ManagedFileRow, String> colCreatedAt;
    @FXML private TableColumn<ManagedFileRow, String> colUpdatedAt;

    private final ManagedFileServices managedFileServices = new ManagedFileServices();
    private final UserServices userServices = new UserServices();
    private final ObservableList<ManagedFileRow> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colUser.setCellValueFactory(c -> c.getValue().userNameProperty());
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
            List<ManagedFileRow> rows = managedFileServices.fetchRows(keyword, typeFilter);
            data.addAll(rows);
            hideMessage();
        } catch (SQLException e) {
            showError("Error loading managed files: " + e.getMessage());
        }
    }

    private String validateForm(boolean isAdd) {
        String userIdText = userIdField.getText() == null ? "" : userIdField.getText().trim();
        String fileName = fileNameField.getText() == null ? "" : fileNameField.getText().trim();
        String filePath = filePathField.getText() == null ? "" : filePathField.getText().trim();
        String fileType = fileTypeField.getText() == null ? "" : fileTypeField.getText().trim();

        if (userIdText.isEmpty()) {
            return "User ID is required.";
        }

        try {
            Long.parseLong(userIdText);
        } catch (NumberFormatException e) {
            return "User ID must be a valid number.";
        }

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
            showError("Please select a managed file.");
            return;
        }

        idField.setText(String.valueOf(row.getId()));
        fileNameField.setText(row.getFileName());
        filePathField.setText(row.getFilePath());
        fileTypeField.setText(row.getFileType());

        try {
            ManagedFile file = managedFileServices.findById(row.getId());
            if (file != null) {
                userIdField.setText(String.valueOf(file.getUserId()));
            } else {
                showError("Selected file was not found.");
                return;
            }
        } catch (SQLException e) {
            showError("Error loading selected file: " + e.getMessage());
            return;
        }

        hideMessage();
    }

    @FXML
    private void onAdd() {
        String err = validateForm(true);
        if (err != null) {
            showError(err);
            return;
        }

        long userId = Long.parseLong(userIdField.getText().trim());

        try {
            if (!userServices.userExistsById(userId)) {
                showError("User ID does not exist.");
                return;
            }

            ManagedFile file = new ManagedFile(
                    userId,
                    fileNameField.getText().trim(),
                    filePathField.getText().trim(),
                    fileTypeField.getText().trim()
            );

            managedFileServices.ajouter(file);
            loadFiles(null, "ALL");
            onClear();
            showSuccess("Managed file added successfully.");

        } catch (SQLException e) {
            showError("Add failed: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        if (idField.getText() == null || idField.getText().trim().isEmpty()) {
            showError("Load a managed file from the table before updating.");
            return;
        }

        String err = validateForm(false);
        if (err != null) {
            showError(err);
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());
        long userId = Long.parseLong(userIdField.getText().trim());
        String filePath = filePathField.getText().trim();

        try {
            if (!userServices.userExistsById(userId)) {
                showError("User ID does not exist.");
                return;
            }

            if (managedFileServices.filePathExistsForOtherFile(filePath, id)) {
                showError("This file path already exists.");
                return;
            }

            ManagedFile file = new ManagedFile(
                    userId,
                    fileNameField.getText().trim(),
                    filePath,
                    fileTypeField.getText().trim()
            );
            file.setId(id);

            managedFileServices.modifier(file);
            loadFiles(null, "ALL");
            onClear();
            showSuccess("Managed file updated successfully.");

        } catch (SQLException e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        ManagedFileRow row = filesTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Please select a managed file to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete managed file?");
        alert.setContentText(row.getFileName());

        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            managedFileServices.supprimer(row.getId());
            loadFiles(searchField.getText(), typeFilterCombo.getValue());
            onClear();
            showSuccess("Managed file deleted successfully.");
        } catch (SQLException e) {
            showError("Delete failed: " + e.getMessage());
        }
    }

    @FXML
    private void onClear() {
        idField.clear();
        userIdField.clear();
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