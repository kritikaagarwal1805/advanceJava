package com.careerbridge.dao;

import com.careerbridge.model.ResumeMetadata;
import com.careerbridge.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for candidate uploaded resume documents metadata.
 */
public class ResumeDAO {

    /**
     * Create a new resume metadata record.
     *
     * @param resume ResumeMetadata entity to persist
     * @return generated resume_id
     * @throws SQLException on database error
     */
    public long create(ResumeMetadata resume) throws SQLException {
        String sql = "INSERT INTO resumes (candidate_user_id, original_filename, stored_filename, storage_path, content_type, file_size, is_primary) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, resume.getCandidateUserId());
            ps.setString(2, resume.getOriginalFilename());
            ps.setString(3, resume.getStoredFilename());
            ps.setString(4, resume.getStoragePath());
            ps.setString(5, resume.getContentType());
            ps.setLong(6, resume.getFileSize());
            ps.setBoolean(7, resume.isPrimary());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    resume.setResumeId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create resume record, no ID obtained.");
    }

    /**
     * Find resume metadata by primary key ID.
     *
     * @param resumeId resume ID
     * @return Optional containing ResumeMetadata if found
     * @throws SQLException on database error
     */
    public Optional<ResumeMetadata> findById(long resumeId) throws SQLException {
        String sql = "SELECT * FROM resumes WHERE resume_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, resumeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieve all resumes uploaded by a candidate, ordered newest first.
     *
     * @param candidateUserId candidate user ID
     * @return List of ResumeMetadata
     * @throws SQLException on database error
     */
    public List<ResumeMetadata> findByCandidateUserId(long candidateUserId) throws SQLException {
        String sql = "SELECT * FROM resumes WHERE candidate_user_id = ? ORDER BY uploaded_at DESC";

        List<ResumeMetadata> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, candidateUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * Find the candidate's active primary resume.
     *
     * @param candidateUserId candidate user ID
     * @return Optional containing primary ResumeMetadata if found
     * @throws SQLException on database error
     */
    public Optional<ResumeMetadata> findPrimaryByCandidateUserId(long candidateUserId) throws SQLException {
        String sql = "SELECT * FROM resumes WHERE candidate_user_id = ? AND is_primary = TRUE LIMIT 1";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, candidateUserId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Set a specific resume as primary for a candidate and unset all others atomically.
     *
     * @param resumeId resume ID to make primary
     * @param candidateUserId candidate user ID
     * @return true if updated successfully
     * @throws SQLException on database error
     */
    public boolean setPrimary(long resumeId, long candidateUserId) throws SQLException {
        String unsetSql = "UPDATE resumes SET is_primary = FALSE WHERE candidate_user_id = ?";
        String setSql = "UPDATE resumes SET is_primary = TRUE WHERE resume_id = ? AND candidate_user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            boolean originalAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement psUnset = conn.prepareStatement(unsetSql)) {
                    psUnset.setLong(1, candidateUserId);
                    psUnset.executeUpdate();
                }

                int updatedRows;
                try (PreparedStatement psSet = conn.prepareStatement(setSql)) {
                    psSet.setLong(1, resumeId);
                    psSet.setLong(2, candidateUserId);
                    updatedRows = psSet.executeUpdate();
                }

                conn.commit();
                return updatedRows > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(originalAutoCommit);
            }
        }
    }

    /**
     * Delete a resume record, verifying candidate ownership.
     *
     * @param resumeId resume ID
     * @param candidateUserId candidate user ID
     * @return true if deleted
     * @throws SQLException on database error
     */
    public boolean delete(long resumeId, long candidateUserId) throws SQLException {
        String sql = "DELETE FROM resumes WHERE resume_id = ? AND candidate_user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, resumeId);
            ps.setLong(2, candidateUserId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Map a ResultSet row to a ResumeMetadata object.
     */
    private ResumeMetadata mapRow(ResultSet rs) throws SQLException {
        ResumeMetadata resume = new ResumeMetadata();
        resume.setResumeId(rs.getLong("resume_id"));
        resume.setCandidateUserId(rs.getLong("candidate_user_id"));
        resume.setOriginalFilename(rs.getString("original_filename"));
        resume.setStoredFilename(rs.getString("stored_filename"));
        resume.setStoragePath(rs.getString("storage_path"));
        resume.setContentType(rs.getString("content_type"));
        resume.setFileSize(rs.getLong("file_size"));
        resume.setPrimary(rs.getBoolean("is_primary"));

        Timestamp uploadedAt = rs.getTimestamp("uploaded_at");
        if (uploadedAt != null) {
            resume.setUploadedAt(uploadedAt.toLocalDateTime());
        }

        return resume;
    }
}
