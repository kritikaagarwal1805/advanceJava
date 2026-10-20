package com.careerbridge.dao;

import com.careerbridge.model.Education;
import com.careerbridge.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Education background records.
 */
public class EducationDAO {

    /**
     * Create a new education record.
     *
     * @param education Education entity to persist
     * @return generated education_id
     * @throws SQLException on database error
     */
    public long create(Education education) throws SQLException {
        String sql = "INSERT INTO education (candidate_user_id, institution, qualification, field_of_study, start_date, end_date, grade) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, education.getCandidateUserId());
            ps.setString(2, education.getInstitution());
            ps.setString(3, education.getQualification());
            ps.setString(4, education.getFieldOfStudy());

            if (education.getStartDate() != null) {
                ps.setDate(5, Date.valueOf(education.getStartDate()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            if (education.getEndDate() != null) {
                ps.setDate(6, Date.valueOf(education.getEndDate()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, education.getGrade());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    education.setEducationId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create education record, no ID obtained.");
    }

    /**
     * Find an education record by ID.
     *
     * @param educationId education ID
     * @return Optional containing Education if found
     * @throws SQLException on database error
     */
    public Optional<Education> findById(long educationId) throws SQLException {
        String sql = "SELECT * FROM education WHERE education_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, educationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieve all education records for a candidate, ordered by start date descending.
     *
     * @param candidateUserId candidate user ID
     * @return List of Education records
     * @throws SQLException on database error
     */
    public List<Education> findByCandidateUserId(long candidateUserId) throws SQLException {
        String sql = "SELECT * FROM education WHERE candidate_user_id = ? ORDER BY start_date DESC";

        List<Education> list = new ArrayList<>();
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
     * Update an education record, ensuring ownership match.
     *
     * @param education Education entity with updated fields
     * @return true if updated
     * @throws SQLException on database error
     */
    public boolean update(Education education) throws SQLException {
        String sql = "UPDATE education SET institution = ?, qualification = ?, field_of_study = ?, " +
                     "start_date = ?, end_date = ?, grade = ? " +
                     "WHERE education_id = ? AND candidate_user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, education.getInstitution());
            ps.setString(2, education.getQualification());
            ps.setString(3, education.getFieldOfStudy());

            if (education.getStartDate() != null) {
                ps.setDate(4, Date.valueOf(education.getStartDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            if (education.getEndDate() != null) {
                ps.setDate(5, Date.valueOf(education.getEndDate()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setString(6, education.getGrade());
            ps.setLong(7, education.getEducationId());
            ps.setLong(8, education.getCandidateUserId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Delete an education record, verifying candidate ownership.
     *
     * @param educationId education ID
     * @param candidateUserId candidate user ID for ownership check
     * @return true if deleted
     * @throws SQLException on database error
     */
    public boolean delete(long educationId, long candidateUserId) throws SQLException {
        String sql = "DELETE FROM education WHERE education_id = ? AND candidate_user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, educationId);
            ps.setLong(2, candidateUserId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Map a ResultSet row to an Education object.
     */
    private Education mapRow(ResultSet rs) throws SQLException {
        Education edu = new Education();
        edu.setEducationId(rs.getLong("education_id"));
        edu.setCandidateUserId(rs.getLong("candidate_user_id"));
        edu.setInstitution(rs.getString("institution"));
        edu.setQualification(rs.getString("qualification"));
        edu.setFieldOfStudy(rs.getString("field_of_study"));

        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            edu.setStartDate(startDate.toLocalDate());
        }

        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            edu.setEndDate(endDate.toLocalDate());
        }

        edu.setGrade(rs.getString("grade"));
        return edu;
    }
}
