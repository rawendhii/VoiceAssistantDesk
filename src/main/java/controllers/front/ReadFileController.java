package controllers.front;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tn.esprit.entities.ManagedFile;
import tn.esprit.services.ManagedFileServices;

import java.sql.SQLException;
import java.util.List;

public class ReadFileController {

    @FXML private TableView<ManagedFile> table;
    @FXML private TableColumn<ManagedFile, String> colName;
    @FXML private TableColumn<ManagedFile, String> colPath;
    @FXML private TableColumn<ManagedFile, String> colType;

    private final ManagedFileServices service = new ManagedFileServices();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFileName()));
        colPath.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFilePath()));
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFileType()));

        load();
    }

    private void load() {
        try {
            List<ManagedFile> files = service.afficher();
            table.setItems(FXCollections.observableArrayList(files));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRefresh() {
        load();
    }
}