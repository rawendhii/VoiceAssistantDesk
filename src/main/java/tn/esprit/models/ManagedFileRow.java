package tn.esprit.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ManagedFileRow {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty userName = new SimpleStringProperty();
    private final StringProperty fileName = new SimpleStringProperty();
    private final StringProperty filePath = new SimpleStringProperty();
    private final StringProperty fileType = new SimpleStringProperty();
    private final StringProperty createdAt = new SimpleStringProperty();
    private final StringProperty updatedAt = new SimpleStringProperty();

    public ManagedFileRow(int id, String userName, String fileName, String filePath,
                          String fileType, String createdAt, String updatedAt) {
        this.id.set(id);
        this.userName.set(userName);
        this.fileName.set(fileName);
        this.filePath.set(filePath);
        this.fileType.set(fileType);
        this.createdAt.set(createdAt);
        this.updatedAt.set(updatedAt);
    }

    public int getId() {
        return id.get();
    }

    public String getUserName() {
        return userName.get();
    }

    public String getFileName() {
        return fileName.get();
    }

    public String getFilePath() {
        return filePath.get();
    }

    public String getFileType() {
        return fileType.get();
    }

    public String getCreatedAt() {
        return createdAt.get();
    }

    public String getUpdatedAt() {
        return updatedAt.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public StringProperty fileTypeProperty() {
        return fileType;
    }

    public StringProperty createdAtProperty() {
        return createdAt;
    }

    public StringProperty updatedAtProperty() {
        return updatedAt;
    }
}