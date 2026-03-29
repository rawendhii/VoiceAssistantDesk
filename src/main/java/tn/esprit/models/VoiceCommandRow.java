package tn.esprit.models;

import javafx.beans.property.*;

public class VoiceCommandRow {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty label = new SimpleStringProperty();
    private final StringProperty keyword = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final BooleanProperty active = new SimpleBooleanProperty();

    public VoiceCommandRow(int id, String label, String keyword, String description, boolean active) {
        this.id.set(id);
        this.label.set(label);
        this.keyword.set(keyword);
        this.description.set(description);
        this.active.set(active);
    }

    public int getId() {
        return id.get();
    }

    public String getLabel() {
        return label.get();
    }

    public String getKeyword() {
        return keyword.get();
    }

    public String getDescription() {
        return description.get();
    }

    public boolean isActive() {
        return active.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty labelProperty() {
        return label;
    }

    public StringProperty keywordProperty() {
        return keyword;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public BooleanProperty activeProperty() {
        return active;
    }
}