package controllers.back;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.CommandHistoryRow;
import tn.esprit.services.CommandHistoryServices;

import java.sql.SQLException;
import java.util.List;

public class CommandsHistoryController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilterCombo;
    @FXML private Label messageLabel;

    @FXML private TableView<CommandHistoryRow> historyTable;
    @FXML private TableColumn<CommandHistoryRow, Integer> colId;
    @FXML private TableColumn<CommandHistoryRow, String> colUser;
    @FXML private TableColumn<CommandHistoryRow, String> colCommand;
    @FXML private TableColumn<CommandHistoryRow, String> colExecutedText;
    @FXML private TableColumn<CommandHistoryRow, String> colStatus;
    @FXML private TableColumn<CommandHistoryRow, String> colExecutedAt;

    private final CommandHistoryServices commandHistoryServices = new CommandHistoryServices();
    private final ObservableList<CommandHistoryRow> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colUser.setCellValueFactory(c -> c.getValue().userNameProperty());
        colCommand.setCellValueFactory(c -> c.getValue().commandLabelProperty());
        colExecutedText.setCellValueFactory(c -> c.getValue().executedTextProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());
        colExecutedAt.setCellValueFactory(c -> c.getValue().executedAtProperty());

        historyTable.setItems(data);

        statusFilterCombo.setItems(FXCollections.observableArrayList("ALL", "SUCCESS", "FAILED"));
        statusFilterCombo.setValue("ALL");

        loadHistory(null, "ALL");
    }

    private void loadHistory(String keyword, String status) {
        data.clear();
        try {
            List<CommandHistoryRow> rows = commandHistoryServices.fetchRows(keyword, status);
            data.addAll(rows);
            hideMessage();
        } catch (SQLException e) {
            showError("Error loading command history: " + e.getMessage());
        }
    }

    @FXML
    private void onSearch() {
        loadHistory(searchField.getText(), statusFilterCombo.getValue());
    }

    @FXML
    private void onRefresh() {
        searchField.clear();
        statusFilterCombo.setValue("ALL");
        loadHistory(null, "ALL");
    }

    @FXML
    private void onDelete() {
        CommandHistoryRow row = historyTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Please select a history record to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete command history record?");
        alert.setContentText("ID: " + row.getId());

        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            commandHistoryServices.supprimer(row.getId());
            loadHistory(searchField.getText(), statusFilterCombo.getValue());
            showSuccess("History record deleted successfully.");
        } catch (SQLException e) {
            showError("Delete failed: " + e.getMessage());
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