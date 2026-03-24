package controllers.back;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tn.esprit.config.SessionManager;

import java.io.IOException;
import java.net.URL;

public class BackLayoutController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        goDashboard();
    }

    @FXML
    private void goDashboard() {
        load("/back/Dashboard.fxml");
    }

    @FXML
    private void goUsers() {
        load("/back/Users_List.fxml");
    }

    @FXML
    private void goCommands() {
        load("/back/Commands_History.fxml");
    }

    @FXML
    private void goFront() {
        load("/front/FrontHome.fxml");
    }
    @FXML private void goRoles()    { 
    	load("/back/Roles_List.fxml"); }
    @FXML private void goSettings() { 
    	load("/back/Settings.fxml"); }

    private void load(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) throw new IllegalStateException("FXML not found: " + path);

            FXMLLoader loader = new FXMLLoader(url);
            Node view = loader.load();
            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + path, e);
        }
    }
    @FXML
    private void onLogout() {
        try {
            SessionManager.clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}