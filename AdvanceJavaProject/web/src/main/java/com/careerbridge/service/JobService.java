package com.careerbridge.service;

import com.careerbridge.dao.CompanyDAO;
import com.careerbridge.dao.JobDAO;
import com.careerbridge.model.Company;
import com.careerbridge.model.Job;
import com.careerbridge.model.JobSearchCriteria;
import java.sql.SQLException;
import java.util.*;

/**
 * Job management and search service.
 */
public class JobService {
    private final JobDAO jobDAO = new JobDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();

    public Job createJob(Job job, long ownerUserId) {
        try {
            Optional<Company> comp = companyDAO.findByOwnerUserId(ownerUserId);
            if (comp.isEmpty()) throw new ServiceException("Create a company profile first.");
            job.setCompanyId(comp.get().getCompanyId());
            jobDAO.create(job);
            return job;
        } catch (SQLException e) { throw new ServiceException("Failed to create job.", e); }
    }

    public boolean updateJob(Job job, long ownerUserId) {
        try {
            if (!isJobOwnedByUser(job.getJobId(), ownerUserId))
                throw new ServiceException("You can only edit your own jobs.");
            return jobDAO.update(job);
        } catch (SQLException e) { throw new ServiceException("Failed to update job.", e); }
    }

    public Optional<Job> getJobById(long jobId) {
        try { return jobDAO.findById(jobId); }
        catch (SQLException e) { throw new ServiceException("Failed to load job.", e); }
    }

    public List<Job> searchJobs(JobSearchCriteria criteria) {
        try { return jobDAO.search(criteria); }
        catch (SQLException e) { throw new ServiceException("Search failed.", e); }
    }

    public int countSearchResults(JobSearchCriteria criteria) {
        try { return jobDAO.countSearch(criteria); }
        catch (SQLException e) { throw new ServiceException("Count failed.", e); }
    }

    public List<Job> getJobsByOwner(long ownerUserId) {
        try { return jobDAO.findByOwnerUserId(ownerUserId); }
        catch (SQLException e) { throw new ServiceException("Failed to load jobs.", e); }
    }

    public boolean updateJobStatus(long jobId, Job.Status status, long ownerUserId) {
        try {
            if (!isJobOwnedByUser(jobId, ownerUserId))
                throw new ServiceException("You can only manage your own jobs.");
            return jobDAO.updateStatus(jobId, status);
        } catch (SQLException e) { throw new ServiceException("Failed to update job status.", e); }
    }

    public List<Job> getRecentJobs(int limit) {
        try { return jobDAO.getRecentJobs(limit); }
        catch (SQLException e) { throw new ServiceException("Failed to load recent jobs.", e); }
    }

    public Map<String, Integer> getJobCountsByStatus() {
        try { return jobDAO.getCountsByStatus(); }
        catch (SQLException e) { throw new ServiceException("Failed to get job stats.", e); }
    }

    public boolean isJobOwnedByUser(long jobId, long ownerUserId) {
        try {
            Optional<Job> job = jobDAO.findById(jobId);
            if (job.isEmpty()) return false;
            Optional<Company> comp = companyDAO.findByOwnerUserId(ownerUserId);
            return comp.isPresent() && comp.get().getCompanyId() == job.get().getCompanyId();
        } catch (SQLException e) { return false; }
    }
}
