package controllers.back;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Role;
import tn.esprit.entities.User;
import tn.esprit.models.UserRow;
import tn.esprit.services.RoleServices;
import tn.esprit.services.UserServices;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class UsersListController {

    @FXML private TextField searchField, idField, fullNameField, emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<Role> roleCombo;
    @FXML private Label messageLabel;

    @FXML private TableView<UserRow> usersTable;
    @FXML private TableColumn<UserRow, Integer> colId;
    @FXML private TableColumn<UserRow, String> colFullName;
    @FXML private TableColumn<UserRow, String> colEmail;
    @FXML private TableColumn<UserRow, String> colRole;

    private int currentUserId;
    private String currentUserRole;

    private final UserServices userServices = new UserServices();
    private final RoleServices roleServices = new RoleServices();
    private final ObservableList<UserRow> data = FXCollections.observableArrayList();

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colFullName.setCellValueFactory(c -> c.getValue().fullNameProperty());
        colEmail.setCellValueFactory(c -> c.getValue().emailProperty());
        colRole.setCellValueFactory(c -> c.getValue().roleNameProperty());

        usersTable.setItems(data);

        if (tn.esprit.config.SessionManager.getUser() != null) {
            currentUserId = tn.esprit.config.SessionManager.getUser().getId();
            currentUserRole = tn.esprit.config.SessionManager.getUser().getRoleName();
        } else {
            currentUserId = -1;
            currentUserRole = "";
        }

        loadRoles();
        loadUsers(null);
    }

    private void loadRoles() {
        try {
            List<Role> roles = roleServices.afficher();
            roleCombo.setItems(FXCollections.observableArrayList(roles));
            if (!roles.isEmpty()) roleCombo.getSelectionModel().select(0);
            hideMessage();
        } catch (SQLException e) {
            showError("Erreur chargement rôles: " + e.getMessage());
        }
    }

    private void loadUsers(String keyword) {
        data.clear();
        try {
            List<UserRow> rows = userServices.fetchUserRows(keyword);
            data.addAll(rows);
            hideMessage();
        } catch (SQLException e) {
            showError("Erreur chargement utilisateurs: " + e.getMessage());
        }
    }

    @FXML
    private void onSearch() {
        loadUsers(searchField.getText());
    }

    @FXML
    private void onRefresh() {
        searchField.clear();
        loadUsers(null);
        onClear();
    }

    @FXML
    private void onLoadSelected() {
        UserRow row = usersTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Veuillez sélectionner un utilisateur.");
            return;
        }
        idField.setText(String.valueOf(row.getId()));
        fullNameField.setText(row.getFullName());
        emailField.setText(row.getEmail());
        passwordField.clear();

        Role match = roleCombo.getItems().stream()
                .filter(r -> r.getName().equalsIgnoreCase(row.getRoleName()))
                .findFirst().orElse(null);
        if (match != null) roleCombo.getSelectionModel().select(match);

        hideMessage();
    }

    @FXML
    private void onAdd() {
        String err = validateForm(true);
        if (err != null) { showError(err); return; }

        Role role = roleCombo.getSelectionModel().getSelectedItem();
        User u = new User(
                fullNameField.getText().trim(),
                emailField.getText().trim(),
                passwordField.getText().trim(),
                role.getId()
        );

        try {
            userServices.ajouter(u);
            loadUsers(null);
            onClear();
            showSuccess("Utilisateur ajouté avec succès.");
        } catch (SQLException e) {
            showError("Ajout impossible: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        if (idField.getText() == null || idField.getText().trim().isEmpty()) {
            showError("Load a user from the table before updating.");
            return;
        }

        String err = validateForm(false);
        if (err != null) {
            showError(err);
            return;
        }

        int id = Integer.parseInt(idField.getText().trim());
        Role role = roleCombo.getSelectionModel().getSelectedItem();
        String email = emailField.getText().trim();

        String password = passwordField.getText() == null ? "" : passwordField.getText().trim();
        if (password.isEmpty()) {
            try {
                password = userServices.getPasswordHashById(id);
                if (password == null) {
                    showError("Unable to retrieve existing password.");
                    return;
                }
            } catch (SQLException e) {
                showError("Error retrieving password: " + e.getMessage());
                return;
            }
        }

        try {
            if (userServices.emailExistsForOtherUser(email, id)) {
                showError("This email is already used by another user.");
                return;
            }

            User u = new User(
                    id,
                    fullNameField.getText().trim(),
                    email,
                    password,
                    role.getId()
            );

            userServices.modifier(u);
            loadUsers(null);
            onClear();
            showSuccess("User updated successfully.");

        } catch (SQLException e) {
            showError("Update failed: " + e.getMessage());
        }
    }
    @FXML
    private void onDelete() {
        UserRow row = usersTable.getSelectionModel().getSelectedItem();
        if (row == null) {
            showError("Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }

        if (row.getId() == currentUserId) {
            showError("Vous ne pouvez pas supprimer votre propre compte.");
            return;
        }

        if ("ADMIN".equalsIgnoreCase(row.getRoleName()) && !"ADMIN".equalsIgnoreCase(currentUserRole)) {
            showError("Seul un ADMIN peut supprimer un autre ADMIN.");
            return;
        }

        if ("ADMIN".equalsIgnoreCase(row.getRoleName())) {
            try {
                int adminsCount = userServices.countAdmins();
                if (adminsCount <= 1) {
                    showError("Impossible de supprimer le dernier ADMIN. Ajoutez un autre ADMIN d'abord.");
                    return;
                }
            } catch (SQLException e) {
                showError("Erreur vérification admin: " + e.getMessage());
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer l'utilisateur ?");
        alert.setContentText(row.getFullName() + " (" + row.getEmail() + ")");
        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        try {
            userServices.supprimer(row.getId());
            loadUsers(null);
            onClear();
            showSuccess("Utilisateur supprimé.");
        } catch (SQLException e) {
            showError("Suppression impossible: " + e.getMessage());
        }
    }

    @FXML
    private void onClear() {
        idField.clear();
        fullNameField.clear();
        emailField.clear();
        passwordField.clear();
        if (!roleCombo.getItems().isEmpty()) roleCombo.getSelectionModel().select(0);
        hideMessage();
    }

    private String validateForm(boolean isAdd) {
        String fullName = fullNameField.getText() == null ? "" : fullNameField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String pass = passwordField.getText() == null ? "" : passwordField.getText().trim();

        if (fullName.length() < 3) return "Nom invalide (minimum 3 caractères).";
        if (!EMAIL_PATTERN.matcher(email).matches()) return "Email invalide.";

        if (roleCombo.getSelectionModel().getSelectedItem() == null) return "Veuillez choisir un rôle.";

        if (isAdd && pass.length() < 6) return "Mot de passe invalide (minimum 6 caractères).";
        if (!isAdd && !pass.isEmpty() && pass.length() < 6) return "Mot de passe invalide (minimum 6 caractères).";

        if (isAdd) {
            try {
                if (userServices.emailExists(email)) return "Cet email existe déjà.";
            } catch (SQLException e) {
                return "Erreur validation email: " + e.getMessage();
            }
        }
        return null;
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