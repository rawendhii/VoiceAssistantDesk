package tn.esprit.services;

import tn.esprit.config.DBConnection;
import tn.esprit.entities.User;
import tn.esprit.models.UserRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServices {

    private final Connection cnx;

    public UserServices() {
        cnx = DBConnection.getInstance().getCnx();
    }

    public int ajouter(User u) throws SQLException {
        String sql = "INSERT INTO users(full_name, email, password_hash, role_id) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setInt(4, u.getRoleId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void modifier(User u) throws SQLException {
        String sql = "UPDATE users SET full_name=?, email=?, password_hash=?, role_id=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setInt(4, u.getRoleId());
            ps.setInt(5, u.getId());
            ps.executeUpdate();
        }
    }

    public boolean modifierInfos(int id, String fullName, String email) throws SQLException {
        String sql = "UPDATE users SET full_name=?, email=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<User> afficher() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, full_name, email, password_hash, role_id FROM users ORDER BY id ASC";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getInt("role_id")
                ));
            }
        }
        return users;
    }

    public List<UserRow> fetchUserRows(String keyword) throws SQLException {
        List<UserRow> rows = new ArrayList<>();

        String baseSql = """
                SELECT u.id, u.full_name, u.email, r.name AS role_name
                FROM users u
                JOIN roles r ON r.id = u.role_id
                """;

        String where = "";
        if (keyword != null && !keyword.trim().isEmpty()) {
            where = " WHERE u.full_name LIKE ? OR u.email LIKE ? ";
        }

        String sql = baseSql + where + " ORDER BY u.id DESC";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            if (!where.isEmpty()) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new UserRow(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("role_name")
                    ));
                }
            }
        }
        return rows;
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email=? LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int countAdmins() throws SQLException {
        String sql = """
            SELECT COUNT(*)
            FROM users u
            JOIN roles r ON r.id = u.role_id
            WHERE UPPER(r.name) = 'ADMIN'
        """;
        try (PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public String getPasswordHashById(int id) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("password_hash") : null;
            }
        }
    }

    public boolean registerUser(String fullName, String email, String passwordHash, int roleId) throws SQLException {
        String sql = "INSERT INTO users(full_name,email,password_hash,role_id) VALUES(?,?,?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            ps.setInt(4, roleId);
            return ps.executeUpdate() > 0;
        }
    }

    public User login(String email, String passwordHash) throws SQLException {
        String sql = """
            SELECT u.id, u.full_name, u.email, u.password_hash, u.role_id, r.name AS role_name
            FROM users u
            JOIN roles r ON r.id = u.role_id
            WHERE u.email = ?
        """;
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String dbPass = rs.getString("password_hash");
                if (!dbPass.equals(passwordHash)) return null;

                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRoleId(rs.getInt("role_id"));
                u.setRoleName(rs.getString("role_name"));
                return u;
            }
        }
    }

    public int getRoleIdByName(String roleName) throws SQLException {
        String sql = "SELECT id FROM roles WHERE UPPER(name)=UPPER(?) LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id") : -1;
            }
        }
    }
    public boolean emailExistsForOtherUser(String email, int currentUserId) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ? AND id <> ? LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, currentUserId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    }