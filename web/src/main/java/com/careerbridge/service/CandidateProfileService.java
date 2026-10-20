package com.careerbridge.service;

import com.careerbridge.dao.*;
import com.careerbridge.model.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Candidate profile, education, and skills management.
 */
public class CandidateProfileService {
    private final CandidateProfileDAO profileDAO = new CandidateProfileDAO();
    private final EducationDAO educationDAO = new EducationDAO();
    private final SkillDAO skillDAO = new SkillDAO();

    public Optional<CandidateProfile> getProfile(long userId) {
        try { return profileDAO.findByUserId(userId); }
        catch (SQLException e) { throw new ServiceException("Failed to load profile.", e); }
    }

    public void saveProfile(CandidateProfile profile) {
        try { profileDAO.createOrUpdate(profile); }
        catch (SQLException e) { throw new ServiceException("Failed to save profile.", e); }
    }

    public List<Education> getEducation(long userId) {
        try { return educationDAO.findByCandidateUserId(userId); }
        catch (SQLException e) { throw new ServiceException("Failed to load education.", e); }
    }

    public Education addEducation(Education edu) {
        try { educationDAO.create(edu); return edu; }
        catch (SQLException e) { throw new ServiceException("Failed to add education.", e); }
    }

    public boolean updateEducation(Education edu, long userId) {
        try {
            Optional<Education> existing = educationDAO.findById(edu.getEducationId());
            if (existing.isEmpty() || existing.get().getCandidateUserId() != userId)
                throw new ServiceException("Education record not found or access denied.");
            return educationDAO.update(edu);
        } catch (SQLException e) { throw new ServiceException("Failed to update education.", e); }
    }

    public boolean deleteEducation(long educationId, long userId) {
        try { return educationDAO.delete(educationId, userId); }
        catch (SQLException e) { throw new ServiceException("Failed to delete education.", e); }
    }

    public List<CandidateSkill> getCandidateSkills(long userId) {
        try { return skillDAO.getSkillsForCandidate(userId); }
        catch (SQLException e) { throw new ServiceException("Failed to load skills.", e); }
    }

    public void addSkill(long userId, long skillId, String proficiencyLevel) {
        CandidateSkill.ProficiencyLevel level;
        try { level = CandidateSkill.ProficiencyLevel.valueOf(proficiencyLevel); }
        catch (Exception e) { level = CandidateSkill.ProficiencyLevel.INTERMEDIATE; }
        try { skillDAO.addSkillToCandidate(userId, skillId, level); }
        catch (SQLException e) { throw new ServiceException("Failed to add skill.", e); }
    }

    public void removeSkill(long userId, long skillId) {
        try { skillDAO.removeSkillFromCandidate(userId, skillId); }
        catch (SQLException e) { throw new ServiceException("Failed to remove skill.", e); }
    }

    public List<Skill> getAllSkills() {
        try { return skillDAO.findAll(); }
        catch (SQLException e) { throw new ServiceException("Failed to load skills.", e); }
    }
}
