package tn.esprit.models;

import javafx.beans.property.*;

public class CommandHistoryRow {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty userName = new SimpleStringProperty();
    private final StringProperty commandLabel = new SimpleStringProperty();
    private final StringProperty executedText = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty executedAt = new SimpleStringProperty();

    public CommandHistoryRow(int id, String userName, String commandLabel, String executedText, String status, String executedAt) {
        this.id.set(id);
        this.userName.set(userName);
        this.commandLabel.set(commandLabel);
        this.executedText.set(executedText);
        this.status.set(status);
        this.executedAt.set(executedAt);
    }

    public int getId() {
        return id.get();
    }

    public String getUserName() {
        return userName.get();
    }

    public String getCommandLabel() {
        return commandLabel.get();
    }

    public String getExecutedText() {
        return executedText.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getExecutedAt() {
        return executedAt.get();
    }

    // 🔥 THIS IS WHAT YOU ARE MISSING
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public StringProperty commandLabelProperty() {
        return commandLabel;
    }

    public StringProperty executedTextProperty() {
        return executedText;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty executedAtProperty() {
        return executedAt;
    }
}