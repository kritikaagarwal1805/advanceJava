package com.careerbridge.dao;

import com.careerbridge.model.CandidateSkill;
import com.careerbridge.model.Skill;
import com.careerbridge.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Skill dictionary and Candidate Skill association operations.
 */
public class SkillDAO {

    /**
     * Retrieve all available skills sorted alphabetically.
     *
     * @return List of all Skills
     * @throws SQLException on database error
     */
    public List<Skill> findAll() throws SQLException {
        String sql = "SELECT * FROM skills ORDER BY name ASC";

        List<Skill> skills = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                skills.add(mapRow(rs));
            }
        }
        return skills;
    }

    /**
     * Find a skill by its primary key ID.
     *
     * @param skillId skill ID
     * @return Optional containing Skill if found
     * @throws SQLException on database error
     */
    public Optional<Skill> findById(long skillId) throws SQLException {
        String sql = "SELECT * FROM skills WHERE skill_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, skillId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Find a skill by its exact name (case-insensitive).
     *
     * @param name skill name
     * @return Optional containing Skill if found
     * @throws SQLException on database error
     */
    public Optional<Skill> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM skills WHERE LOWER(name) = LOWER(?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Create a new skill in the global catalog.
     *
     * @param skill Skill entity to persist
     * @return generated skill_id
     * @throws SQLException on database error
     */
    public long create(Skill skill) throws SQLException {
        String sql = "INSERT INTO skills (name) VALUES (?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, skill.getName());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    skill.setSkillId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create skill, no ID obtained.");
    }

    /**
     * Retrieve all skills associated with a candidate, including skill name and proficiency level.
     *
     * @param candidateUserId candidate user ID
     * @return List of CandidateSkill objects
     * @throws SQLException on database error
     */
    public List<CandidateSkill> getSkillsForCandidate(long candidateUserId) throws SQLException {
        String sql = "SELECT cs.candidate_user_id, cs.skill_id, cs.proficiency_level, s.name AS skill_name " +
                     "FROM candidate_skills cs " +
                     "JOIN skills s ON cs.skill_id = s.skill_id " +
                     "WHERE cs.candidate_user_id = ? " +
                     "ORDER BY s.name ASC";

        List<CandidateSkill> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, candidateUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCandidateSkillRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * Associate a skill with a candidate using INSERT IGNORE.
     *
     * @param candidateUserId candidate user ID
     * @param skillId skill ID
     * @param level proficiency level
     * @return true if row was inserted
     * @throws SQLException on database error
     */
    public boolean addSkillToCandidate(long candidateUserId, long skillId, CandidateSkill.ProficiencyLevel level) throws SQLException {
        String sql = "INSERT IGNORE INTO candidate_skills (candidate_user_id, skill_id, proficiency_level) " +
                     "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, candidateUserId);
            ps.setLong(2, skillId);
            ps.setString(3, level != null ? level.name() : CandidateSkill.ProficiencyLevel.INTERMEDIATE.name());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Remove a skill from a candidate.
     *
     * @param candidateUserId candidate user ID
     * @param skillId skill ID
     * @return true if deleted
     * @throws SQLException on database error
     */
    public boolean removeSkillFromCandidate(long candidateUserId, long skillId) throws SQLException {
        String sql = "DELETE FROM candidate_skills WHERE candidate_user_id = ? AND skill_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, candidateUserId);
            ps.setLong(2, skillId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Update proficiency level of a candidate's skill.
     *
     * @param candidateUserId candidate user ID
     * @param skillId skill ID
     * @param level new proficiency level
     * @return true if updated
     * @throws SQLException on database error
     */
    public boolean updateCandidateSkillLevel(long candidateUserId, long skillId, CandidateSkill.ProficiencyLevel level) throws SQLException {
        String sql = "UPDATE candidate_skills SET proficiency_level = ? " +
                     "WHERE candidate_user_id = ? AND skill_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, level != null ? level.name() : CandidateSkill.ProficiencyLevel.INTERMEDIATE.name());
            ps.setLong(2, candidateUserId);
            ps.setLong(3, skillId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Map ResultSet row to Skill entity.
     */
    private Skill mapRow(ResultSet rs) throws SQLException {
        Skill skill = new Skill();
        skill.setSkillId(rs.getLong("skill_id"));
        skill.setName(rs.getString("name"));
        return skill;
    }

    /**
     * Map ResultSet row to CandidateSkill entity with skill name.
     */
    private CandidateSkill mapCandidateSkillRow(ResultSet rs) throws SQLException {
        CandidateSkill cs = new CandidateSkill();
        cs.setCandidateUserId(rs.getLong("candidate_user_id"));
        cs.setSkillId(rs.getLong("skill_id"));
        cs.setSkillName(rs.getString("skill_name"));

        String level = rs.getString("proficiency_level");
        if (level != null) {
            cs.setProficiencyLevel(CandidateSkill.ProficiencyLevel.valueOf(level));
        }

        return cs;
    }
}
