package controllers.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import tn.esprit.config.SessionManager;
import tn.esprit.entities.User;

import java.io.IOException;

public class FrontLayoutController {

    @FXML private StackPane contentArea;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = SessionManager.getUser();
        if (currentUser != null) {
            goHome();
        }
    }

    @FXML
    private void goHome() {
        loadView("/front/FrontHome.fxml");
    }

    @FXML
    private void goVoice() {
        loadView("/front/Voice.fxml");
    }

    @FXML
    private void goFiles() {
        loadView("/front/UserManagedFiles.fxml");
    }

    @FXML
    private void goProfile() {
        loadView("/front/Profile.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node node = loader.load();
            contentArea.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToUserFiles() {
        loadView("/front/UserFiles.fxml");
    }
}