package controllers.front;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class FrontLayoutController {

    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        setContent("/front/FrontHome.fxml");
    }

    private void setContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load: " + fxmlPath, e);
        }
    }

    @FXML private void goHome()   { setContent("/front/FrontHome.fxml"); }
    @FXML private void goVoice()  { setContent("/front/Voice.fxml"); }
    @FXML private void goFiles()  { setContent("/front/Files.fxml"); }
    @FXML private void goProfile(){ setContent("/front/Profile.fxml"); }

    
}