package app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        URL fxml = getClass().getResource("/front/Login.fxml");

        if (fxml == null) {
            throw new IllegalStateException("Login.fxml not found. Check path: /front/Login.fxml");
        }

        stage.setTitle("Voice Assistant Desktop");
        stage.setScene(new Scene(FXMLLoader.load(fxml)));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}