package com.careerbridge.dao;

import com.careerbridge.model.CandidateProfile;
import com.careerbridge.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Data Access Object for Candidate Profile operations.
 * Handles candidate biographical and portfolio details with UPSERT support.
 */
public class CandidateProfileDAO {

    /**
     * Create or update a candidate profile using MySQL UPSERT (INSERT ON DUPLICATE KEY UPDATE).
     *
     * @param profile CandidateProfile entity to persist
     * @return true if inserted or updated
     * @throws SQLException on database error
     */
    public boolean createOrUpdate(CandidateProfile profile) throws SQLException {
        String sql = "INSERT INTO candidate_profiles (user_id, headline, summary, phone, location, portfolio_url, linkedin_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "headline = VALUES(headline), " +
                     "summary = VALUES(summary), " +
                     "phone = VALUES(phone), " +
                     "location = VALUES(location), " +
                     "portfolio_url = VALUES(portfolio_url), " +
                     "linkedin_url = VALUES(linkedin_url)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, profile.getUserId());
            ps.setString(2, profile.getHeadline());
            ps.setString(3, profile.getSummary());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getLocation());
            ps.setString(6, profile.getPortfolioUrl());
            ps.setString(7, profile.getLinkedinUrl());

            int affected = ps.executeUpdate();

            // Set profileId from generated keys if inserted
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    profile.setProfileId(rs.getLong(1));
                }
            }

            // If updated and profileId not yet populated on entity, fetch it
            if (profile.getProfileId() <= 0) {
                findByUserId(profile.getUserId()).ifPresent(p -> profile.setProfileId(p.getProfileId()));
            }

            return affected >= 0;
        }
    }

    /**
     * Find a candidate profile by user ID.
     *
     * @param userId user ID
     * @return Optional containing the CandidateProfile if found
     * @throws SQLException on database error
     */
    public Optional<CandidateProfile> findByUserId(long userId) throws SQLException {
        String sql = "SELECT * FROM candidate_profiles WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Find a candidate profile by primary key profile ID.
     *
     * @param profileId profile ID
     * @return Optional containing CandidateProfile if found
     * @throws SQLException on database error
     */
    public Optional<CandidateProfile> findById(long profileId) throws SQLException {
        String sql = "SELECT * FROM candidate_profiles WHERE profile_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, profileId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Delete candidate profile for a user.
     *
     * @param userId user ID
     * @return true if deleted
     * @throws SQLException on database error
     */
    public boolean delete(long userId) throws SQLException {
        String sql = "DELETE FROM candidate_profiles WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Map a ResultSet row to a CandidateProfile object.
     */
    private CandidateProfile mapRow(ResultSet rs) throws SQLException {
        CandidateProfile profile = new CandidateProfile();
        profile.setProfileId(rs.getLong("profile_id"));
        profile.setUserId(rs.getLong("user_id"));
        profile.setHeadline(rs.getString("headline"));
        profile.setSummary(rs.getString("summary"));
        profile.setPhone(rs.getString("phone"));
        profile.setLocation(rs.getString("location"));
        profile.setPortfolioUrl(rs.getString("portfolio_url"));
        profile.setLinkedinUrl(rs.getString("linkedin_url"));

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            profile.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return profile;
    }
}
