package tn.esprit.services;

import tn.esprit.config.DBConnection;
import tn.esprit.entities.ManagedFile;
import tn.esprit.models.ManagedFileRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManagedFileServices {

    private final Connection cnx;

    public ManagedFileServices() {
        cnx = DBConnection.getInstance().getCnx();
    }

    public void ajouter(ManagedFile f) throws SQLException {
        String sql = "INSERT INTO managed_files(user_id, file_name, file_path, file_type) VALUES (?,?,?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setLong(1, f.getUserId());
            ps.setString(2, f.getFileName());
            ps.setString(3, f.getFilePath());
            ps.setString(4, f.getFileType());
            ps.executeUpdate();
        }
    }

    public void modifier(ManagedFile f) throws SQLException {
        String sql = "UPDATE managed_files SET user_id=?, file_name=?, file_path=?, file_type=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setLong(1, f.getUserId());
            ps.setString(2, f.getFileName());
            ps.setString(3, f.getFilePath());
            ps.setString(4, f.getFileType());
            ps.setInt(5, f.getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM managed_files WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public ManagedFile findById(int id) throws SQLException {
        String sql = """
            SELECT id, user_id, file_name, file_path, file_type, created_at, updated_at
            FROM managed_files
            WHERE id = ?
        """;

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ManagedFile(
                            rs.getInt("id"),
                            rs.getLong("user_id"),
                            rs.getString("file_name"),
                            rs.getString("file_path"),
                            rs.getString("file_type"),
                            rs.getTimestamp("created_at"),
                            rs.getTimestamp("updated_at")
                    );
                }
            }
        }

        return null;
    }

    public List<ManagedFile> afficher() throws SQLException {
        List<ManagedFile> list = new ArrayList<>();

        String sql = """
            SELECT id, user_id, file_name, file_path, file_type, created_at, updated_at
            FROM managed_files
            ORDER BY id DESC
        """;

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ManagedFile(
                        rs.getInt("id"),
                        rs.getLong("user_id"),
                        rs.getString("file_name"),
                        rs.getString("file_path"),
                        rs.getString("file_type"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                ));
            }
        }

        return list;
    }

    public List<ManagedFileRow> fetchRows(String keyword, String typeFilter) throws SQLException {
        List<ManagedFileRow> rows = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT mf.id,
                   u.full_name AS user_name,
                   mf.file_name,
                   mf.file_path,
                   mf.file_type,
                   mf.created_at,
                   mf.updated_at
            FROM managed_files mf
            JOIN users u ON u.id = mf.user_id
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (u.full_name LIKE ? OR mf.file_name LIKE ? OR mf.file_path LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }

        if (typeFilter != null && !typeFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(typeFilter)) {
            sql.append(" AND UPPER(mf.file_type) = UPPER(?) ");
            params.add(typeFilter.trim());
        }

        sql.append(" ORDER BY mf.updated_at DESC");

        try (PreparedStatement ps = cnx.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new ManagedFileRow(
                            rs.getInt("id"),
                            rs.getString("user_name"),
                            rs.getString("file_name"),
                            rs.getString("file_path"),
                            rs.getString("file_type"),
                            rs.getTimestamp("created_at").toString(),
                            rs.getTimestamp("updated_at").toString()
                    ));
                }
            }
        }

        return rows;
    }

    public boolean filePathExists(String filePath) throws SQLException {
        String sql = "SELECT 1 FROM managed_files WHERE file_path = ? LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, filePath.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean filePathExistsForOtherFile(String filePath, int currentId) throws SQLException {
        String sql = "SELECT 1 FROM managed_files WHERE file_path = ? AND id <> ? LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, filePath.trim());
            ps.setInt(2, currentId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    public List<ManagedFileRow> fetchRowsByUserId(long userId, String keyword, String typeFilter) throws SQLException {
        List<ManagedFileRow> rows = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT mf.id,
                   u.full_name AS user_name,
                   mf.file_name,
                   mf.file_path,
                   mf.file_type,
                   mf.created_at,
                   mf.updated_at
            FROM managed_files mf
            JOIN users u ON u.id = mf.user_id
            WHERE mf.user_id = ?
        """);

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (mf.file_name LIKE ? OR mf.file_path LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }

        if (typeFilter != null && !typeFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(typeFilter)) {
            sql.append(" AND UPPER(mf.file_type) = UPPER(?) ");
            params.add(typeFilter.trim());
        }

        sql.append(" ORDER BY mf.updated_at DESC");

        try (PreparedStatement ps = cnx.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new ManagedFileRow(
                            rs.getInt("id"),
                            rs.getString("user_name"),
                            rs.getString("file_name"),
                            rs.getString("file_path"),
                            rs.getString("file_type"),
                            rs.getTimestamp("created_at").toString(),
                            rs.getTimestamp("updated_at").toString()
                    ));
                }
            }
        }

        return rows;
    }
}