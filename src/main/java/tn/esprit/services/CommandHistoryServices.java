package tn.esprit.services;

import tn.esprit.config.DBConnection;
import tn.esprit.entities.CommandHistory;
import tn.esprit.models.CommandHistoryRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandHistoryServices {

    private final Connection cnx;

    public CommandHistoryServices() {
        cnx = DBConnection.getInstance().getCnx();
    }

    public void ajouter(CommandHistory h) throws SQLException {
        String sql = "INSERT INTO command_history(user_id, command_id, executed_text, status) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setLong(1, h.getUserId());
            ps.setInt(2, h.getCommandId());
            ps.setString(3, h.getExecutedText());
            ps.setString(4, h.getStatus());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM command_history WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<CommandHistory> afficher() throws SQLException {
        List<CommandHistory> list = new ArrayList<>();

        String sql = """
            SELECT id, user_id, command_id, executed_text, status, executed_at
            FROM command_history
            ORDER BY executed_at DESC
        """;

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new CommandHistory(
                        rs.getInt("id"),
                        rs.getLong("user_id"),
                        rs.getInt("command_id"),
                        rs.getString("executed_text"),
                        rs.getString("status"),
                        rs.getTimestamp("executed_at")
                ));
            }
        }

        return list;
    }

    public List<CommandHistoryRow> fetchRows(String keyword, String statusFilter) throws SQLException {
        List<CommandHistoryRow> rows = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT ch.id,
                   u.full_name AS user_name,
                   vc.label AS command_label,
                   ch.executed_text,
                   ch.status,
                   ch.executed_at
            FROM command_history ch
            JOIN users u ON u.id = ch.user_id
            JOIN voice_commands vc ON vc.id = ch.command_id
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (u.full_name LIKE ? OR vc.label LIKE ? OR ch.executed_text LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }

        if (statusFilter != null && !statusFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(statusFilter)) {
            sql.append(" AND UPPER(ch.status) = UPPER(?) ");
            params.add(statusFilter.trim());
        }

        sql.append(" ORDER BY ch.executed_at DESC");

        try (PreparedStatement ps = cnx.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new CommandHistoryRow(
                            rs.getInt("id"),
                            rs.getString("user_name"),
                            rs.getString("command_label"),
                            rs.getString("executed_text"),
                            rs.getString("status"),
                            rs.getTimestamp("executed_at").toString()
                    ));
                }
            }
        }

        return rows;
    }
}