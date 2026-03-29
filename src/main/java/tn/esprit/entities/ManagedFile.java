package tn.esprit.entities;

import java.sql.Timestamp;

public class ManagedFile {

    private int id;
    private long userId;
    private String fileName;
    private String filePath;
    private String fileType;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private String userName;

    public ManagedFile() {
    }

    public ManagedFile(int id, long userId, String fileName, String filePath, String fileType, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ManagedFile(long userId, String fileName, String filePath, String fileType) {
        this.userId = userId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}