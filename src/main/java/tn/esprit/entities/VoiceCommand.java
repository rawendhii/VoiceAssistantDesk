package tn.esprit.entities;

public class VoiceCommand {

    private int id;
    private String label;
    private String keyword;
    private String description;
    private boolean active;

    public VoiceCommand() {
    }

    public VoiceCommand(int id, String label, String keyword, String description, boolean active) {
        this.id = id;
        this.label = label;
        this.keyword = keyword;
        this.description = description;
        this.active = active;
    }

    public VoiceCommand(String label, String keyword, String description, boolean active) {
        this.label = label;
        this.keyword = keyword;
        this.description = description;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}