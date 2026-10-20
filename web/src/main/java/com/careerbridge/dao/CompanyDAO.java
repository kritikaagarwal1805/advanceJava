package com.careerbridge.dao;

import com.careerbridge.model.Company;
import com.careerbridge.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Company business profile operations.
 */
public class CompanyDAO {

    /**
     * Insert a new company profile.
     *
     * @param company Company entity to persist
     * @return generated company_id
     * @throws SQLException on database error
     */
    public long create(Company company) throws SQLException {
        String sql = "INSERT INTO companies (owner_user_id, name, description, website, industry, location, logo_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, company.getOwnerUserId());
            ps.setString(2, company.getName());
            ps.setString(3, company.getDescription());
            ps.setString(4, company.getWebsite());
            ps.setString(5, company.getIndustry());
            ps.setString(6, company.getLocation());
            ps.setString(7, company.getLogoUrl());
            ps.setString(8, company.getStatus() != null ? company.getStatus().name() : Company.Status.ACTIVE.name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    company.setCompanyId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create company, no ID obtained.");
    }

    /**
     * Find a company by its unique ID.
     *
     * @param companyId company ID
     * @return Optional containing the Company if found
     * @throws SQLException on database error
     */
    public Optional<Company> findById(long companyId) throws SQLException {
        String sql = "SELECT * FROM companies WHERE company_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, companyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Find a company owned by a specific employer user ID.
     *
     * @param ownerUserId user_id of the employer
     * @return Optional containing Company if found
     * @throws SQLException on database error
     */
    public Optional<Company> findByOwnerUserId(long ownerUserId) throws SQLException {
        String sql = "SELECT * FROM companies WHERE owner_user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, ownerUserId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Update an existing company's editable profile details.
     *
     * @param company Company entity with updated fields
     * @return true if updated
     * @throws SQLException on database error
     */
    public boolean update(Company company) throws SQLException {
        String sql = "UPDATE companies SET name = ?, description = ?, website = ?, industry = ?, " +
                     "location = ?, logo_url = ?, status = ? " +
                     "WHERE company_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, company.getName());
            ps.setString(2, company.getDescription());
            ps.setString(3, company.getWebsite());
            ps.setString(4, company.getIndustry());
            ps.setString(5, company.getLocation());
            ps.setString(6, company.getLogoUrl());
            ps.setString(7, company.getStatus() != null ? company.getStatus().name() : Company.Status.ACTIVE.name());
            ps.setLong(8, company.getCompanyId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Update the active/inactive/pending status of a company.
     *
     * @param companyId company ID
     * @param status new status
     * @return true if updated
     * @throws SQLException on database error
     */
    public boolean updateStatus(long companyId, Company.Status status) throws SQLException {
        String sql = "UPDATE companies SET status = ? WHERE company_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setLong(2, companyId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Retrieve a paginated list of companies ordered by registration date.
     *
     * @param page page number (1-based)
     * @param pageSize records per page
     * @return List of companies
     * @throws SQLException on database error
     */
    public List<Company> findAll(int page, int pageSize) throws SQLException {
        String sql = "SELECT * FROM companies ORDER BY created_at DESC LIMIT ? OFFSET ?";
        int offset = (Math.max(1, page) - 1) * Math.max(1, pageSize);

        List<Company> companies = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    companies.add(mapRow(rs));
                }
            }
        }
        return companies;
    }

    /**
     * Count total registered companies in the system.
     *
     * @return total company count
     * @throws SQLException on database error
     */
    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM companies";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Map a ResultSet row to a Company object.
     */
    private Company mapRow(ResultSet rs) throws SQLException {
        Company company = new Company();
        company.setCompanyId(rs.getLong("company_id"));
        company.setOwnerUserId(rs.getLong("owner_user_id"));
        company.setName(rs.getString("name"));
        company.setDescription(rs.getString("description"));
        company.setWebsite(rs.getString("website"));
        company.setIndustry(rs.getString("industry"));
        company.setLocation(rs.getString("location"));
        company.setLogoUrl(rs.getString("logo_url"));

        String status = rs.getString("status");
        if (status != null) {
            company.setStatus(Company.Status.valueOf(status));
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            company.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            company.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return company;
    }
}
