package tn.esprit.services;

import tn.esprit.config.DBConnection;
import tn.esprit.entities.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleServices {

    public void ajouter(Role r) throws SQLException {
        String sql = "INSERT INTO roles(name) VALUES(?)";
        try (PreparedStatement ps = DBConnection.getInstance().getCnx().prepareStatement(sql)) {
            ps.setString(1, r.getName());
            ps.executeUpdate();
        }
    }

    public void modifier(Role r) throws SQLException {
        String sql = "UPDATE roles SET name=? WHERE id=?";
        try (PreparedStatement ps = DBConnection.getInstance().getCnx().prepareStatement(sql)) {
            ps.setString(1, r.getName());
            ps.setInt(2, r.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM roles WHERE id=?";
        try (PreparedStatement ps = DBConnection.getInstance().getCnx().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Role> afficher() throws SQLException {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT id, name FROM roles ORDER BY id ASC";
        try (Statement st = DBConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Role(rs.getInt("id"), rs.getString("name")));
            }
        }
        return list;
    }

    public boolean nameExists(String name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM roles WHERE UPPER(name)=UPPER(?)";
        try (PreparedStatement ps = DBConnection.getInstance().getCnx().prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public int countUsersByRole(int roleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role_id=?";
        try (PreparedStatement ps = DBConnection.getInstance().getCnx().prepareStatement(sql)) {
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
    public boolean nameExistsForOtherRole(String name, int currentRoleId) throws SQLException {
        String sql = "SELECT 1 FROM roles WHERE UPPER(name)=UPPER(?) AND id <> ? LIMIT 1";
        try (PreparedStatement ps = DBConnection.getInstance().getCnx().prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setInt(2, currentRoleId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}