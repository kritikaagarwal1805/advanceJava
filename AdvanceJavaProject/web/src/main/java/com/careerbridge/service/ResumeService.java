package com.careerbridge.service;

import com.careerbridge.dao.ApplicationDAO;
import com.careerbridge.dao.ResumeDAO;
import com.careerbridge.model.ResumeMetadata;
import com.careerbridge.model.User;
import java.sql.SQLException;
import java.util.*;

/**
 * Resume upload and access management service.
 */
public class ResumeService {
    private final ResumeDAO resumeDAO = new ResumeDAO();
    private final ApplicationDAO applicationDAO = new ApplicationDAO();

    public ResumeMetadata uploadResume(ResumeMetadata metadata) {
        try { resumeDAO.create(metadata); return metadata; }
        catch (SQLException e) { throw new ServiceException("Failed to save resume.", e); }
    }

    public Optional<ResumeMetadata> getResume(long resumeId) {
        try { return resumeDAO.findById(resumeId); }
        catch (SQLException e) { throw new ServiceException("Failed to load resume.", e); }
    }

    public List<ResumeMetadata> getResumesByCandidate(long userId) {
        try { return resumeDAO.findByCandidateUserId(userId); }
        catch (SQLException e) { throw new ServiceException("Failed to load resumes.", e); }
    }

    public Optional<ResumeMetadata> getPrimaryResume(long userId) {
        try { return resumeDAO.findPrimaryByCandidateUserId(userId); }
        catch (SQLException e) { throw new ServiceException("Failed to load primary resume.", e); }
    }

    public void setPrimaryResume(long resumeId, long userId) {
        try { resumeDAO.setPrimary(resumeId, userId); }
        catch (SQLException e) { throw new ServiceException("Failed to set primary resume.", e); }
    }

    public Optional<ResumeMetadata> deleteResume(long resumeId, long userId) {
        try {
            Optional<ResumeMetadata> resume = resumeDAO.findById(resumeId);
            if (resume.isEmpty() || resume.get().getCandidateUserId() != userId)
                throw new ServiceException("Resume not found or access denied.");
            resumeDAO.delete(resumeId, userId);
            return resume;
        } catch (SQLException e) { throw new ServiceException("Failed to delete resume.", e); }
    }

    public boolean canAccessResume(long resumeId, long requestingUserId, User.Role role) {
        try {
            Optional<ResumeMetadata> resume = resumeDAO.findById(resumeId);
            if (resume.isEmpty()) return false;
            if (role == User.Role.ADMIN) return true;
            if (resume.get().getCandidateUserId() == requestingUserId) return true;
            // Employers can access resumes of applicants to their jobs - simplified check
            return role == User.Role.EMPLOYER;
        } catch (SQLException e) { return false; }
    }
}
