package com.careerbridge.dao;

import com.careerbridge.model.ApplicationStatusHistory;
import com.careerbridge.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Application Status transition history / audit logs.
 */
public class ApplicationStatusHistoryDAO {

    /**
     * Record a new status change event in the audit trail.
     *
     * @param history ApplicationStatusHistory entity to persist
     * @return generated history_id
     * @throws SQLException on database error
     */
    public long create(ApplicationStatusHistory history) throws SQLException {
        String sql = "INSERT INTO application_status_history (application_id, old_status, new_status, changed_by_user_id, notes) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, history.getApplicationId());
            ps.setString(2, history.getOldStatus());
            ps.setString(3, history.getNewStatus());

            if (history.getChangedByUserId() > 0) {
                ps.setLong(4, history.getChangedByUserId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }

            ps.setString(5, history.getNotes());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    history.setHistoryId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create application status history, no ID obtained.");
    }

    /**
     * Retrieve complete status transition history for an application, ordered newest first.
     *
     * @param applicationId application ID
     * @return List of ApplicationStatusHistory records
     * @throws SQLException on database error
     */
    public List<ApplicationStatusHistory> findByApplicationId(long applicationId) throws SQLException {
        String sql = "SELECT * FROM application_status_history WHERE application_id = ? ORDER BY changed_at DESC";

        List<ApplicationStatusHistory> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * Map a ResultSet row to an ApplicationStatusHistory object.
     */
    private ApplicationStatusHistory mapRow(ResultSet rs) throws SQLException {
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        history.setHistoryId(rs.getLong("history_id"));
        history.setApplicationId(rs.getLong("application_id"));
        history.setOldStatus(rs.getString("old_status"));
        history.setNewStatus(rs.getString("new_status"));

        long changedBy = rs.getLong("changed_by_user_id");
        if (!rs.wasNull()) {
            history.setChangedByUserId(changedBy);
        }

        history.setNotes(rs.getString("notes"));

        Timestamp changedAt = rs.getTimestamp("changed_at");
        if (changedAt != null) {
            history.setChangedAt(changedAt.toLocalDateTime());
        }

        return history;
    }
}
