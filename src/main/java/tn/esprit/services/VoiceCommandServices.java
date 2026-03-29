package tn.esprit.services;

import tn.esprit.config.DBConnection;
import tn.esprit.entities.VoiceCommand;
import tn.esprit.models.VoiceCommandRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoiceCommandServices {

    private final Connection cnx;

    public VoiceCommandServices() {
        cnx = DBConnection.getInstance().getCnx();
    }

    public void ajouter(VoiceCommand c) throws SQLException {
        String sql = "INSERT INTO voice_commands(label, keyword, description, active) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, c.getLabel());
            ps.setString(2, c.getKeyword());
            ps.setString(3, c.getDescription());
            ps.setBoolean(4, c.isActive());
            ps.executeUpdate();
        }
    }

    public void modifier(VoiceCommand c) throws SQLException {
        String sql = "UPDATE voice_commands SET label=?, keyword=?, description=?, active=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, c.getLabel());
            ps.setString(2, c.getKeyword());
            ps.setString(3, c.getDescription());
            ps.setBoolean(4, c.isActive());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM voice_commands WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<VoiceCommand> afficher() throws SQLException {
        List<VoiceCommand> list = new ArrayList<>();
        String sql = "SELECT id, label, keyword, description, active FROM voice_commands ORDER BY id ASC";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new VoiceCommand(
                        rs.getInt("id"),
                        rs.getString("label"),
                        rs.getString("keyword"),
                        rs.getString("description"),
                        rs.getBoolean("active")
                ));
            }
        }

        return list;
    }

    public List<VoiceCommandRow> fetchRows(String keywordSearch) throws SQLException {
        List<VoiceCommandRow> rows = new ArrayList<>();

        String baseSql = "SELECT id, label, keyword, description, active FROM voice_commands";
        String where = "";
        if (keywordSearch != null && !keywordSearch.trim().isEmpty()) {
            where = " WHERE label LIKE ? OR keyword LIKE ? OR description LIKE ? ";
        }
        String sql = baseSql + where + " ORDER BY id DESC";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            if (!where.isEmpty()) {
                String like = "%" + keywordSearch.trim() + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new VoiceCommandRow(
                            rs.getInt("id"),
                            rs.getString("label"),
                            rs.getString("keyword"),
                            rs.getString("description"),
                            rs.getBoolean("active")
                    ));
                }
            }
        }

        return rows;
    }

    public boolean keywordExists(String keyword) throws SQLException {
        String sql = "SELECT 1 FROM voice_commands WHERE UPPER(keyword)=UPPER(?) LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, keyword.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean keywordExistsForOtherCommand(String keyword, int currentId) throws SQLException {
        String sql = "SELECT 1 FROM voice_commands WHERE UPPER(keyword)=UPPER(?) AND id <> ? LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, keyword.trim());
            ps.setInt(2, currentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}