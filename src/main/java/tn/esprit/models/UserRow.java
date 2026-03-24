package tn.esprit.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserRow {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty roleName = new SimpleStringProperty();

    public UserRow(int id, String fullName, String email, String roleName) {
        this.id.set(id);
        this.fullName.set(fullName);
        this.email.set(email);
        this.roleName.set(roleName);
    }

    public int getId() { return id.get(); }
    public String getFullName() { return fullName.get(); }
    public String getEmail() { return email.get(); }
    public String getRoleName() { return roleName.get(); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty emailProperty() { return email; }
    public StringProperty roleNameProperty() { return roleName; }
}