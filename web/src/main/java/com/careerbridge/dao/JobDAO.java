package com.careerbridge.dao;

import com.careerbridge.model.Job;
import com.careerbridge.model.JobSearchCriteria;
import com.careerbridge.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Data Access Object for Job vacancy operations.
 * Implements full CRUD, dynamic faceted search, and analytics queries.
 */
public class JobDAO {

    /**
     * Insert a new job vacancy posting.
     *
     * @param job Job entity to persist
     * @return generated job_id
     * @throws SQLException on database error
     */
    public long create(Job job) throws SQLException {
        String sql = "INSERT INTO jobs (company_id, title, description, requirements, responsibilities, " +
                     "location, job_type, work_mode, experience_level, salary_min, salary_max, " +
                     "currency, application_deadline, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, job.getCompanyId());
            ps.setString(2, job.getTitle());
            ps.setString(3, job.getDescription());
            ps.setString(4, job.getRequirements());
            ps.setString(5, job.getResponsibilities());
            ps.setString(6, job.getLocation());
            ps.setString(7, job.getJobType() != null ? job.getJobType().name() : Job.JobType.FULL_TIME.name());
            ps.setString(8, job.getWorkMode() != null ? job.getWorkMode().name() : Job.WorkMode.ONSITE.name());
            ps.setString(9, job.getExperienceLevel() != null ? job.getExperienceLevel().name() : Job.ExperienceLevel.ENTRY.name());

            if (job.getSalaryMin() != null) {
                ps.setBigDecimal(10, job.getSalaryMin());
            } else {
                ps.setNull(10, Types.DECIMAL);
            }

            if (job.getSalaryMax() != null) {
                ps.setBigDecimal(11, job.getSalaryMax());
            } else {
                ps.setNull(11, Types.DECIMAL);
            }

            ps.setString(12, job.getCurrency() != null ? job.getCurrency() : "INR");

            if (job.getApplicationDeadline() != null) {
                ps.setDate(13, Date.valueOf(job.getApplicationDeadline()));
            } else {
                ps.setNull(13, Types.DATE);
            }

            ps.setString(14, job.getStatus() != null ? job.getStatus().name() : Job.Status.DRAFT.name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    job.setJobId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create job, no ID obtained.");
    }

    /**
     * Find a job by its unique ID, joining company details.
     *
     * @param jobId primary key
     * @return Optional containing the Job if found
     * @throws SQLException on database error
     */
    public Optional<Job> findById(long jobId) throws SQLException {
        String sql = "SELECT j.*, c.name AS company_name, c.logo_url AS company_logo_url " +
                     "FROM jobs j " +
                     "LEFT JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE j.job_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowWithCompany(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Update all editable fields of a job posting.
     *
     * @param job Job entity with updated values
     * @return true if updated successfully
     * @throws SQLException on database error
     */
    public boolean update(Job job) throws SQLException {
        String sql = "UPDATE jobs SET company_id = ?, title = ?, description = ?, requirements = ?, " +
                     "responsibilities = ?, location = ?, job_type = ?, work_mode = ?, " +
                     "experience_level = ?, salary_min = ?, salary_max = ?, currency = ?, " +
                     "application_deadline = ?, status = ? " +
                     "WHERE job_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, job.getCompanyId());
            ps.setString(2, job.getTitle());
            ps.setString(3, job.getDescription());
            ps.setString(4, job.getRequirements());
            ps.setString(5, job.getResponsibilities());
            ps.setString(6, job.getLocation());
            ps.setString(7, job.getJobType() != null ? job.getJobType().name() : Job.JobType.FULL_TIME.name());
            ps.setString(8, job.getWorkMode() != null ? job.getWorkMode().name() : Job.WorkMode.ONSITE.name());
            ps.setString(9, job.getExperienceLevel() != null ? job.getExperienceLevel().name() : Job.ExperienceLevel.ENTRY.name());

            if (job.getSalaryMin() != null) {
                ps.setBigDecimal(10, job.getSalaryMin());
            } else {
                ps.setNull(10, Types.DECIMAL);
            }

            if (job.getSalaryMax() != null) {
                ps.setBigDecimal(11, job.getSalaryMax());
            } else {
                ps.setNull(11, Types.DECIMAL);
            }

            ps.setString(12, job.getCurrency() != null ? job.getCurrency() : "INR");

            if (job.getApplicationDeadline() != null) {
                ps.setDate(13, Date.valueOf(job.getApplicationDeadline()));
            } else {
                ps.setNull(13, Types.DATE);
            }

            ps.setString(14, job.getStatus() != null ? job.getStatus().name() : Job.Status.DRAFT.name());
            ps.setLong(15, job.getJobId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Update only the publication status of a job posting.
     *
     * @param jobId primary key
     * @param status new status
     * @return true if row was updated
     * @throws SQLException on database error
     */
    public boolean updateStatus(long jobId, Job.Status status) throws SQLException {
        String sql = "UPDATE jobs SET status = ? WHERE job_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setLong(2, jobId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Execute a faceted dynamic search with pagination support.
     *
     * @param criteria search and filter criteria
     * @return List of matching Job objects with company names populated
     * @throws SQLException on database error
     */
    public List<Job> search(JobSearchCriteria criteria) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT j.*, c.name AS company_name, c.logo_url AS company_logo_url " +
                "FROM jobs j " +
                "LEFT JOIN companies c ON j.company_id = c.company_id " +
                "WHERE 1=1"
        );
        List<Object> params = buildFilterParams(sql, criteria);

        sql.append(" ORDER BY j.created_at DESC LIMIT ? OFFSET ?");
        params.add(criteria.getPageSize());
        params.add(criteria.getOffset());

        List<Job> jobs = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            bindParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    jobs.add(mapRowWithCompany(rs));
                }
            }
        }
        return jobs;
    }

    /**
     * Count total matching records for a given search criteria (used for pagination calculation).
     *
     * @param criteria search and filter criteria
     * @return count of matching jobs
     * @throws SQLException on database error
     */
    public int countSearch(JobSearchCriteria criteria) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM jobs j WHERE 1=1");
        List<Object> params = buildFilterParams(sql, criteria);

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            bindParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Retrieve all jobs published by a specific company.
     *
     * @param companyId primary key of company
     * @return List of jobs for the company
     * @throws SQLException on database error
     */
    public List<Job> findByCompanyId(long companyId) throws SQLException {
        String sql = "SELECT j.*, c.name AS company_name, c.logo_url AS company_logo_url " +
                     "FROM jobs j " +
                     "LEFT JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE j.company_id = ? " +
                     "ORDER BY j.created_at DESC";

        List<Job> jobs = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    jobs.add(mapRowWithCompany(rs));
                }
            }
        }
        return jobs;
    }

    /**
     * Retrieve all jobs belonging to the company owned by the specified employer user.
     *
     * @param ownerUserId user_id of the employer
     * @return List of jobs owned by that employer
     * @throws SQLException on database error
     */
    public List<Job> findByOwnerUserId(long ownerUserId) throws SQLException {
        String sql = "SELECT j.*, c.name AS company_name, c.logo_url AS company_logo_url " +
                     "FROM jobs j " +
                     "INNER JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE c.owner_user_id = ? " +
                     "ORDER BY j.created_at DESC";

        List<Job> jobs = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, ownerUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    jobs.add(mapRowWithCompany(rs));
                }
            }
        }
        return jobs;
    }

    /**
     * Aggregate job counts grouped by status for dashboard metrics.
     *
     * @return Map of status name to count
     * @throws SQLException on database error
     */
    public Map<String, Integer> getCountsByStatus() throws SQLException {
        String sql = "SELECT status, COUNT(*) AS cnt FROM jobs GROUP BY status";
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
     * Fetch the most recently published jobs up to the specified limit.
     *
     * @param limit maximum jobs to return
     * @return List of latest published jobs
     * @throws SQLException on database error
     */
    public List<Job> getRecentJobs(int limit) throws SQLException {
        String sql = "SELECT j.*, c.name AS company_name, c.logo_url AS company_logo_url " +
                     "FROM jobs j " +
                     "LEFT JOIN companies c ON j.company_id = c.company_id " +
                     "WHERE j.status = 'PUBLISHED' " +
                     "ORDER BY j.created_at DESC " +
                     "LIMIT ?";

        List<Job> jobs = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    jobs.add(mapRowWithCompany(rs));
                }
            }
        }
        return jobs;
    }

    /**
     * Builds dynamic WHERE clause filters and populates the parameter list.
     */
    private List<Object> buildFilterParams(StringBuilder sql, JobSearchCriteria criteria) {
        List<Object> params = new ArrayList<>();
        if (criteria == null) {
            return params;
        }

        if (criteria.getStatus() != null && !criteria.getStatus().trim().isEmpty()) {
            sql.append(" AND j.status = ?");
            params.add(criteria.getStatus().trim());
        }

        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            sql.append(" AND (LOWER(j.title) LIKE ? OR LOWER(j.description) LIKE ?)");
            String pattern = "%" + criteria.getKeyword().trim().toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
        }

        if (criteria.getLocation() != null && !criteria.getLocation().trim().isEmpty()) {
            sql.append(" AND LOWER(j.location) LIKE ?");
            params.add("%" + criteria.getLocation().trim().toLowerCase() + "%");
        }

        if (criteria.getJobType() != null && !criteria.getJobType().trim().isEmpty()) {
            sql.append(" AND j.job_type = ?");
            params.add(criteria.getJobType().trim());
        }

        if (criteria.getExperienceLevel() != null && !criteria.getExperienceLevel().trim().isEmpty()) {
            sql.append(" AND j.experience_level = ?");
            params.add(criteria.getExperienceLevel().trim());
        }

        if (criteria.getWorkMode() != null && !criteria.getWorkMode().trim().isEmpty()) {
            sql.append(" AND j.work_mode = ?");
            params.add(criteria.getWorkMode().trim());
        }

        if (criteria.getCompanyId() != null) {
            sql.append(" AND j.company_id = ?");
            params.add(criteria.getCompanyId());
        }

        if (criteria.getSalaryMin() != null) {
            sql.append(" AND (j.salary_max >= ? OR j.salary_min >= ?)");
            params.add(criteria.getSalaryMin());
            params.add(criteria.getSalaryMin());
        }

        if (criteria.getSalaryMax() != null) {
            sql.append(" AND (j.salary_min <= ? OR (j.salary_min IS NULL AND j.salary_max <= ?))");
            params.add(criteria.getSalaryMax());
            params.add(criteria.getSalaryMax());
        }

        return params;
    }

    /**
     * Binds heterogeneous parameters to a PreparedStatement.
     */
    private void bindParameters(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            int idx = i + 1;
            if (param instanceof String s) {
                ps.setString(idx, s);
            } else if (param instanceof Integer n) {
                ps.setInt(idx, n);
            } else if (param instanceof Long l) {
                ps.setLong(idx, l);
            } else if (param instanceof BigDecimal bd) {
                ps.setBigDecimal(idx, bd);
            } else {
                ps.setObject(idx, param);
            }
        }
    }

    /**
     * Map a basic ResultSet row to a Job entity.
     */
    private Job mapRow(ResultSet rs) throws SQLException {
        Job job = new Job();
        job.setJobId(rs.getLong("job_id"));
        job.setCompanyId(rs.getLong("company_id"));
        job.setTitle(rs.getString("title"));
        job.setDescription(rs.getString("description"));
        job.setRequirements(rs.getString("requirements"));
        job.setResponsibilities(rs.getString("responsibilities"));
        job.setLocation(rs.getString("location"));

        String jt = rs.getString("job_type");
        if (jt != null) {
            job.setJobType(Job.JobType.valueOf(jt));
        }

        String wm = rs.getString("work_mode");
        if (wm != null) {
            job.setWorkMode(Job.WorkMode.valueOf(wm));
        }

        String el = rs.getString("experience_level");
        if (el != null) {
            job.setExperienceLevel(Job.ExperienceLevel.valueOf(el));
        }

        job.setSalaryMin(rs.getBigDecimal("salary_min"));
        job.setSalaryMax(rs.getBigDecimal("salary_max"));
        job.setCurrency(rs.getString("currency"));

        Date deadline = rs.getDate("application_deadline");
        if (deadline != null) {
            job.setApplicationDeadline(deadline.toLocalDate());
        }

        String st = rs.getString("status");
        if (st != null) {
            job.setStatus(Job.Status.valueOf(st));
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            job.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            job.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return job;
    }

    /**
     * Map a ResultSet row to a Job entity and populate joined company fields.
     */
    private Job mapRowWithCompany(ResultSet rs) throws SQLException {
        Job job = mapRow(rs);
        try {
            job.setCompanyName(rs.getString("company_name"));
        } catch (SQLException ignored) {
            // column not present in projection
        }
        try {
            job.setCompanyLogoUrl(rs.getString("company_logo_url"));
        } catch (SQLException ignored) {
            // column not present in projection
        }
        return job;
    }
}
