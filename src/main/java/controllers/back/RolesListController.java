package controllers.back;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Role;
import tn.esprit.models.RoleRow;
import tn.esprit.services.RoleServices;

import java.sql.SQLException;
import java.util.List;

public class RolesListController {

    @FXML private TextField searchField, idField, nameField;
    @FXML private Label messageLabel;

    @FXML private TableView<RoleRow> rolesTable;
    @FXML private TableColumn<RoleRow, Integer> colId;
    @FXML private TableColumn<RoleRow, String> colName;

    private final RoleServices roleServices = new RoleServices();
    private final ObservableList<RoleRow> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
    	colId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
    	colName.setCellValueFactory(c -> c.getValue().nameProperty());
        rolesTable.setItems(data);

        loadRoles(null);
    }

    private void loadRoles(String keyword) {
        data.clear();
        try {
            List<Role> roles = roleServices.afficher();
            for (Role r : roles) {
                if (keyword == null || keyword.trim().isEmpty()
                        || r.getName().toLowerCase().contains(keyword.trim().toLowerCase())) {
                    data.add(new RoleRow(r.getId(), r.getName()));
                }
            }
            hideMessage();
        } catch (SQLException e) {
            showError("Erreur chargement rôles: " + e.getMessage());
        }
    }

    @FXML private void onSearch() { loadRoles(searchField.getText()); }

    @FXML
    private void onRefresh() {
        searchField.clear();
        loadRoles(null);
        onClear();
    }

    @FXML
    private void onLoadSelected() {
        RoleRow row = rolesTable.getSelectionModel().getSelectedItem();
        if (row == null) { showError("Veuillez sélectionner un rôle."); return; }
        idField.setText(String.valueOf(row.getId()));
        nameField.setText(row.getName());
        hideMessage();
    }

    @FXML
    private void onAdd() {
        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        if (name.length() < 3) { showError("Nom invalide (min 3 caractères)."); return; }

        try {
            if (roleServices.nameExists(name)) { showError("Ce rôle existe déjà."); return; }
            roleServices.ajouter(new Role(name));
            loadRoles(null);
            onClear();
            showSuccess("Rôle ajouté.");
        } catch (SQLException e) {
            showError("Ajout impossible: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        if (idField.getText() == null || idField.getText().trim().isEmpty()) {
            showError("Load a role from the table before updating.");
            return;
        }

        String name = nameField.getText() == null ? "" : nameField.getText().trim();
        if (name.length() < 3) {
            showError("Invalid name (min 3 characters).");
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());

        try {
            if (roleServices.nameExistsForOtherRole(name, id)) {
                showError("This role name already exists.");
                return;
            }

            roleServices.modifier(new Role(id, name));

            loadRoles(null);
            onClear();
            showSuccess("Role updated successfully.");

        } catch (SQLException e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        RoleRow row = rolesTable.getSelectionModel().getSelectedItem();
        if (row == null) { showError("Veuillez sélectionner un rôle à supprimer."); return; }

        try {
            int used = roleServices.countUsersByRole(row.getId());
            if (used > 0) {
                showError("Impossible de supprimer: ce rôle est utilisé par " + used + " utilisateur(s).");
                return;
            }
        } catch (SQLException e) {
            showError("Erreur vérification relation: " + e.getMessage());
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le rôle ?");
        alert.setContentText(row.getName());
        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        try {
            roleServices.supprimer(row.getId());
            loadRoles(null);
            onClear();
            showSuccess("Rôle supprimé.");
        } catch (SQLException e) {
            showError("Suppression impossible: " + e.getMessage());
        }
    }

    @FXML
    private void onClear() {
        idField.clear();
        nameField.clear();
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

    private void hideMessage() { messageLabel.setVisible(false); }
}