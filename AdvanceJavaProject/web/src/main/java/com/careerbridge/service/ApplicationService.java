package com.careerbridge.service;

import com.careerbridge.dao.*;
import com.careerbridge.model.*;
import com.careerbridge.util.DatabaseUtil;
import java.sql.*;
import java.util.*;

/**
 * Job application workflow service.
 * Handles apply, status transitions, withdrawal, and access checks.
 */
public class ApplicationService {
    private final ApplicationDAO applicationDAO = new ApplicationDAO();
    private final ApplicationStatusHistoryDAO historyDAO = new ApplicationStatusHistoryDAO();
    private final JobDAO jobDAO = new JobDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();

    /**
     * Submit a job application with transactional integrity.
     */
    public JobApplication apply(long jobId, long candidateUserId, Long resumeId, String coverLetter) {
        Connection conn = null;
        try {
            // Validate job is open
            Optional<Job> job = jobDAO.findById(jobId);
            if (job.isEmpty()) throw new ServiceException("Job not found.");
            if (!job.get().isOpen()) throw new ServiceException("This job is no longer accepting applications.");

            // Check for existing application
            Optional<JobApplication> existing = applicationDAO.findByJobIdAndCandidateId(jobId, candidateUserId);
            if (existing.isPresent()) throw new ServiceException("You have already applied to this job.");

            JobApplication app = new JobApplication();
            app.setJobId(jobId);
            app.setCandidateUserId(candidateUserId);
            app.setResumeId(resumeId);
            app.setCoverLetter(coverLetter);
            app.setStatus(JobApplication.Status.SUBMITTED);

            applicationDAO.create(app);

            // Record in history
            ApplicationStatusHistory history = new ApplicationStatusHistory();
            history.setApplicationId(app.getApplicationId());
            history.setOldStatus(null);
            history.setNewStatus("SUBMITTED");
            history.setChangedByUserId(candidateUserId);
            history.setNotes("Application submitted");
            historyDAO.create(history);

            return app;
        } catch (SQLException e) {
            // Handle duplicate key (concurrent submission)
            if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
                throw new ServiceException("You have already applied to this job.");
            }
            throw new ServiceException("Failed to submit application.", e);
        }
    }

    public Optional<JobApplication> getApplicationById(long applicationId) {
        try { return applicationDAO.findById(applicationId); }
        catch (SQLException e) { throw new ServiceException("Failed to load application.", e); }
    }

    public List<JobApplication> getApplicationsByCandidate(long userId) {
        try { return applicationDAO.findByCandidateUserId(userId); }
        catch (SQLException e) { throw new ServiceException("Failed to load applications.", e); }
    }

    public List<JobApplication> getApplicationsByJob(long jobId, String statusFilter) {
        try { return applicationDAO.findByJobId(jobId, statusFilter); }
        catch (SQLException e) { throw new ServiceException("Failed to load applicants.", e); }
    }

    /**
     * Update application status with transition validation and history tracking.
     */
    public boolean updateStatus(long applicationId, JobApplication.Status newStatus,
                                long changedByUserId, String notes) {
        try {
            Optional<JobApplication> opt = applicationDAO.findById(applicationId);
            if (opt.isEmpty()) throw new ServiceException("Application not found.");
            JobApplication app = opt.get();

            Set<JobApplication.Status> allowed = JobApplication.getAllowedTransitions(app.getStatus());
            if (!allowed.contains(newStatus))
                throw new ServiceException("Cannot transition from " + app.getStatus() + " to " + newStatus);

            String oldStatus = app.getStatus().name();
            boolean updated = applicationDAO.updateStatus(applicationId, newStatus);

            if (updated) {
                ApplicationStatusHistory history = new ApplicationStatusHistory();
                history.setApplicationId(applicationId);
                history.setOldStatus(oldStatus);
                history.setNewStatus(newStatus.name());
                history.setChangedByUserId(changedByUserId);
                history.setNotes(notes);
                historyDAO.create(history);
            }
            return updated;
        } catch (SQLException e) { throw new ServiceException("Failed to update status.", e); }
    }

    public boolean withdrawApplication(long applicationId, long candidateUserId) {
        try {
            Optional<JobApplication> opt = applicationDAO.findById(applicationId);
            if (opt.isEmpty()) throw new ServiceException("Application not found.");
            JobApplication app = opt.get();
            if (app.getCandidateUserId() != candidateUserId)
                throw new ServiceException("You can only withdraw your own applications.");

            Set<JobApplication.Status> allowed = JobApplication.getAllowedTransitions(app.getStatus());
            if (!allowed.contains(JobApplication.Status.WITHDRAWN))
                throw new ServiceException("This application cannot be withdrawn in its current status.");

            return updateStatus(applicationId, JobApplication.Status.WITHDRAWN, candidateUserId, "Withdrawn by candidate");
        } catch (ServiceException e) { throw e; }
        catch (Exception e) { throw new ServiceException("Failed to withdraw application.", e); }
    }

    public boolean canCandidateApply(long jobId, long candidateUserId) {
        try {
            Optional<JobApplication> existing = applicationDAO.findByJobIdAndCandidateId(jobId, candidateUserId);
            return existing.isEmpty();
        } catch (SQLException e) { return false; }
    }

    public Map<String, Integer> getApplicationCountsByStatus() {
        try { return applicationDAO.getCountsByStatus(); }
        catch (SQLException e) { throw new ServiceException("Failed to get stats.", e); }
    }

    public Map<String, Integer> getApplicationCountsForJob(long jobId) {
        try { return applicationDAO.getCountsByStatusForJob(jobId); }
        catch (SQLException e) { throw new ServiceException("Failed to get stats.", e); }
    }

    public Map<String, Integer> getApplicationCountsForEmployer(long ownerUserId) {
        try { return applicationDAO.getCountsByStatusForEmployer(ownerUserId); }
        catch (SQLException e) { throw new ServiceException("Failed to get stats.", e); }
    }

    public List<ApplicationStatusHistory> getStatusHistory(long applicationId) {
        try { return historyDAO.findByApplicationId(applicationId); }
        catch (SQLException e) { throw new ServiceException("Failed to load history.", e); }
    }

    public boolean isApplicationAccessibleByEmployer(long applicationId, long ownerUserId) {
        try {
            Optional<JobApplication> app = applicationDAO.findById(applicationId);
            if (app.isEmpty()) return false;
            Optional<Job> job = jobDAO.findById(app.get().getJobId());
            if (job.isEmpty()) return false;
            Optional<Company> company = companyDAO.findByOwnerUserId(ownerUserId);
            return company.isPresent() && company.get().getCompanyId() == job.get().getCompanyId();
        } catch (SQLException e) { return false; }
    }
}
