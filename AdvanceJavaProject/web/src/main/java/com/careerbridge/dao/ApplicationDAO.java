package com.careerbridge.dao;

import com.careerbridge.model.JobApplication;
import com.careerbridge.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Data Access Object for Job Application operations.
 * Handles candidate submissions, status workflows, and employer/admin statistics.
 */
public class ApplicationDAO {

    /**
     * Create a new job application.
     *
     * @param application JobApplication entity to persist
     * @return generated application_id
     * @throws SQLException on database error (e.g. unique constraint violation)
     */
    public long create(JobApplication application) throws SQLException {
        String sql = "INSERT INTO applications (job_id, candidate_user_id, resume_id, cover_letter, status) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, application.getJobId());
            ps.setLong(2, application.getCandidateUserId());

            if (application.getResumeId() != null) {
                ps.setLong(3, application.getResumeId());
            } else {
                ps.setNull(3, Types.BIGINT);
            }

            ps.setString(4, application.getCoverLetter());
            ps.setString(5, application.getStatus() != null ? application.getStatus().name() : JobApplication.Status.SUBMITTED.name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    application.setApplicationId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create application, no ID obtained.");
    }

    /**
     * Find an application by its ID, with job title, company name, and candidate info joined.
     *
     * @param applicationId application primary key
     * @return Optional containing the JobApplication with details if found
     * @throws SQLException on database error
     */
    public Optional<JobApplication> findById(long applicationId) throws SQLException {
        String sql = "SELECT a.*, j.title AS job_title, c.name AS company_name, " +
                     "u.full_name AS candidate_name, u.email AS candidate_email " +
                     "FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "JOIN users u ON a.candidate_user_id = u.user_id " +
                     "WHERE a.application_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowWithDetails(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieve all applications submitted by a candidate, ordered newest first.
     * Includes job title and company name via JOIN.
     *
     * @param candidateUserId user ID of candidate
     * @return List of JobApplications with details
     * @throws SQLException on database error
     */
    public List<JobApplication> findByCandidateUserId(long candidateUserId) throws SQLException {
        String sql = "SELECT a.*, j.title AS job_title, c.name AS company_name, " +
                     "u.full_name AS candidate_name, u.email AS candidate_email " +
                     "FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "JOIN users u ON a.candidate_user_id = u.user_id " +
                     "WHERE a.candidate_user_id = ? " +
                     "ORDER BY a.applied_at DESC";

        List<JobApplication> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, candidateUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowWithDetails(rs));
                }
            }
        }
        return list;
    }

    /**
     * Retrieve all applications for a specific job with optional status filter.
     * Includes candidate details via JOIN.
     *
     * @param jobId primary key of job
     * @param statusFilter optional status filter (e.g. "SHORTLISTED")
     * @return List of JobApplications with details
     * @throws SQLException on database error
     */
    public List<JobApplication> findByJobId(long jobId, String statusFilter) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT a.*, j.title AS job_title, c.name AS company_name, " +
                "u.full_name AS candidate_name, u.email AS candidate_email " +
                "FROM applications a " +
                "JOIN jobs j ON a.job_id = j.job_id " +
                "JOIN companies c ON j.company_id = c.company_id " +
                "JOIN users u ON a.candidate_user_id = u.user_id " +
                "WHERE a.job_id = ?"
        );

        boolean hasStatus = statusFilter != null && !statusFilter.trim().isEmpty();
        if (hasStatus) {
            sql.append(" AND a.status = ?");
        }
        sql.append(" ORDER BY a.applied_at DESC");

        List<JobApplication> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setLong(1, jobId);
            if (hasStatus) {
                ps.setString(2, statusFilter.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowWithDetails(rs));
                }
            }
        }
        return list;
    }

    /**
     * Check if a candidate has already applied to a specific job (duplicate prevention).
     *
     * @param jobId job ID
     * @param candidateUserId candidate user ID
     * @return Optional containing JobApplication if already exists
     * @throws SQLException on database error
     */
    public Optional<JobApplication> findByJobIdAndCandidateId(long jobId, long candidateUserId) throws SQLException {
        String sql = "SELECT a.*, j.title AS job_title, c.name AS company_name, " +
                     "u.full_name AS candidate_name, u.email AS candidate_email " +
                     "FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "JOIN users u ON a.candidate_user_id = u.user_id " +
                     "WHERE a.job_id = ? AND a.candidate_user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            ps.setLong(2, candidateUserId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowWithDetails(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Update the workflow status of an application.
     *
     * @param applicationId application ID
     * @param newStatus new status enum
     * @return true if updated
     * @throws SQLException on database error
     */
    public boolean updateStatus(long applicationId, JobApplication.Status newStatus) throws SQLException {
        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus.name());
            ps.setLong(2, applicationId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Count total applications submitted for a job.
     *
     * @param jobId job ID
     * @return count of applications
     * @throws SQLException on database error
     */
    public int countByJobId(long jobId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applications WHERE job_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Count total applications submitted by a specific candidate.
     *
     * @param candidateUserId user ID
     * @return count of applications
     * @throws SQLException on database error
     */
    public int countByCandidateUserId(long candidateUserId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applications WHERE candidate_user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, candidateUserId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Get aggregate application counts by status across all jobs for admin dashboard.
     *
     * @return Map of status name to count
     * @throws SQLException on database error
     */
    public Map<String, Integer> getCountsByStatus() throws SQLException {
        String sql = "SELECT status, COUNT(*) AS cnt FROM applications GROUP BY status";
        Map<String, Integer> counts = new LinkedHashMap<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                counts.put(rs.getString("status"), rs.getInt("cnt"));
            }
        }
        return counts;
    }

    /**
     * Get application status breakdown for a specific job.
     *
     * @param jobId job ID
     * @return Map of status name to count
     * @throws SQLException on database error
     */
    public Map<String, Integer> getCountsByStatusForJob(long jobId) throws SQLException {
        String sql = "SELECT status, COUNT(*) AS cnt FROM applications WHERE job_id = ? GROUP BY status";
        Map<String, Integer> counts = new LinkedHashMap<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    counts.put(rs.getString("status"), rs.getInt("cnt"));
                }
            }
        }
        return counts;
    }

    /**
     * Get application status breakdown across all jobs owned by a specific employer.
     *
     * @param ownerUserId employer user ID
     * @return Map of status name to count
     * @throws SQLException on database error
     */
    public Map<String, Integer> getCountsByStatusForEmployer(long ownerUserId) throws SQLException {
        String sql = "SELECT a.status, COUNT(*) AS cnt " +
                     "FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.job_id " +
                     "JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE c.owner_user_id = ? " +
                     "GROUP BY a.status";

        Map<String, Integer> counts = new LinkedHashMap<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, ownerUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    counts.put(rs.getString("status"), rs.getInt("cnt"));
                }
            }
        }
        return counts;
    }

    /**
     * Map basic ResultSet row to JobApplication entity.
     */
    private JobApplication mapRow(ResultSet rs) throws SQLException {
        JobApplication app = new JobApplication();
        app.setApplicationId(rs.getLong("application_id"));
        app.setJobId(rs.getLong("job_id"));
        app.setCandidateUserId(rs.getLong("candidate_user_id"));

        long resumeId = rs.getLong("resume_id");
        if (!rs.wasNull()) {
            app.setResumeId(resumeId);
        } else {
            app.setResumeId(null);
        }

        app.setCoverLetter(rs.getString("cover_letter"));

        String status = rs.getString("status");
        if (status != null) {
            app.setStatus(JobApplication.Status.valueOf(status));
        }

        Timestamp appliedAt = rs.getTimestamp("applied_at");
        if (appliedAt != null) {
            app.setAppliedAt(appliedAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            app.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return app;
    }

    /**
     * Map ResultSet row to JobApplication with transient display fields populated.
     */
    private JobApplication mapRowWithDetails(ResultSet rs) throws SQLException {
        JobApplication app = mapRow(rs);
        try {
            app.setJobTitle(rs.getString("job_title"));
        } catch (SQLException ignored) {
            // column not in projection
        }
        try {
            app.setCompanyName(rs.getString("company_name"));
        } catch (SQLException ignored) {
            // column not in projection
        }
        try {
            app.setCandidateName(rs.getString("candidate_name"));
        } catch (SQLException ignored) {
            // column not in projection
        }
        try {
            app.setCandidateEmail(rs.getString("candidate_email"));
        } catch (SQLException ignored) {
            // column not in projection
        }
        return app;
    }
}
