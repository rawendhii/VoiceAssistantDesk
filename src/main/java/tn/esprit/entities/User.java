package tn.esprit.entities;

public class User {

    private int id;
    private String fullName;
    private String email;
    private String passwordHash;
    private int roleId;

    private String roleName;

    public User() {}

    public User(String fullName, String email, String passwordHash, int roleId) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
    }

    public User(int id, String fullName, String email, String passwordHash, int roleId) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
    }

    public User(int id, String fullName, String email, int roleId, String roleName) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}