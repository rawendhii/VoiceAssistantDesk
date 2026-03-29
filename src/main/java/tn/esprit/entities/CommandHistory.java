package tn.esprit.entities;

import java.sql.Timestamp;

public class CommandHistory {

    private int id;
    private long userId;
    private int commandId;
    private String executedText;
    private String status;
    private Timestamp executedAt;

    private String userName;
    private String commandLabel;

    public CommandHistory() {
    }

    public CommandHistory(int id, long userId, int commandId, String executedText, String status, Timestamp executedAt) {
        this.id = id;
        this.userId = userId;
        this.commandId = commandId;
        this.executedText = executedText;
        this.status = status;
        this.executedAt = executedAt;
    }

    public CommandHistory(long userId, int commandId, String executedText, String status) {
        this.userId = userId;
        this.commandId = commandId;
        this.executedText = executedText;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public String getExecutedText() {
        return executedText;
    }

    public void setExecutedText(String executedText) {
        this.executedText = executedText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Timestamp executedAt) {
        this.executedAt = executedAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommandLabel() {
        return commandLabel;
    }

    public void setCommandLabel(String commandLabel) {
        this.commandLabel = commandLabel;
    }
}