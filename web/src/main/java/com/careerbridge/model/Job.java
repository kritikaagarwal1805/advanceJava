package com.careerbridge.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a job posting in the CareerBridge system.
 */
public class Job implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum JobType {
        FULL_TIME, PART_TIME, INTERNSHIP, CONTRACT
    }

    public enum WorkMode {
        ONSITE, REMOTE, HYBRID
    }

    public enum ExperienceLevel {
        ENTRY, MID, SENIOR, EXECUTIVE
    }

    public enum Status {
        DRAFT, PUBLISHED, CLOSED, DEACTIVATED
    }

    private long jobId;
    private long companyId;
    private String title;
    private String description;
    private String requirements;
    private String responsibilities;
    private String location;
    private JobType jobType;
    private WorkMode workMode;
    private ExperienceLevel experienceLevel;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency = "INR";
    private LocalDate applicationDeadline;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Transient fields for display in listings
    private transient String companyName;
    private transient String companyLogoUrl;

    public Job() {
        this.currency = "INR";
        this.status = Status.DRAFT;
    }

    public Job(long companyId, String title, String description, String requirements,
               String responsibilities, String location, JobType jobType, WorkMode workMode,
               ExperienceLevel experienceLevel, BigDecimal salaryMin, BigDecimal salaryMax,
               String currency, LocalDate applicationDeadline, Status status) {
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.responsibilities = responsibilities;
        this.location = location;
        this.jobType = jobType;
        this.workMode = workMode;
        this.experienceLevel = experienceLevel;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.currency = currency != null ? currency : "INR";
        this.applicationDeadline = applicationDeadline;
        this.status = status != null ? status : Status.DRAFT;
    }

    /**
     * Checks if the job is currently open for applications.
     * A job is open if status is PUBLISHED and deadline is null or on/after today.
     *
     * @return true if open, false otherwise
     */
    public boolean isOpen() {
        if (status != Status.PUBLISHED) {
            return false;
        }
        return applicationDeadline == null || !applicationDeadline.isBefore(LocalDate.now());
    }

    // Getters and Setters
    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public void setWorkMode(WorkMode workMode) {
        this.workMode = workMode;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDate applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public void setCompanyLogoUrl(String companyLogoUrl) {
        this.companyLogoUrl = companyLogoUrl;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId=" + jobId +
                ", companyId=" + companyId +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", jobType=" + jobType +
                ", workMode=" + workMode +
                ", experienceLevel=" + experienceLevel +
                ", status=" + status +
                ", deadline=" + applicationDeadline +
                '}';
    }
}
