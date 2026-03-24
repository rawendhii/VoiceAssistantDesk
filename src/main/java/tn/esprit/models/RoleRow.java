package tn.esprit.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RoleRow {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();

    public RoleRow(int id, String name) {
        this.id.set(id);
        this.name.set(name);
    }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }
}