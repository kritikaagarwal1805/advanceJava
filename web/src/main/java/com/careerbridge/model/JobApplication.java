package com.careerbridge.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents a job application submitted by a candidate.
 */
public class JobApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status {
        SUBMITTED, UNDER_REVIEW, SHORTLISTED, INTERVIEW, SELECTED, REJECTED, WITHDRAWN
    }

    private long applicationId;
    private long jobId;
    private long candidateUserId;
    private Long resumeId; // Nullable
    private String coverLetter;
    private Status status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    // Transient fields for display
    private transient String jobTitle;
    private transient String companyName;
    private transient String candidateName;
    private transient String candidateEmail;

    public JobApplication() {
        this.status = Status.SUBMITTED;
    }

    public JobApplication(long jobId, long candidateUserId, Long resumeId, String coverLetter) {
        this.jobId = jobId;
        this.candidateUserId = candidateUserId;
        this.resumeId = resumeId;
        this.coverLetter = coverLetter;
        this.status = Status.SUBMITTED;
    }

    public JobApplication(long applicationId, long jobId, long candidateUserId, Long resumeId,
                          String coverLetter, Status status, LocalDateTime appliedAt,
                          LocalDateTime updatedAt) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.candidateUserId = candidateUserId;
        this.resumeId = resumeId;
        this.coverLetter = coverLetter;
        this.status = status;
        this.appliedAt = appliedAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Returns the set of valid next statuses from the given status.
     *
     * Rules:
     * - SUBMITTED -> UNDER_REVIEW, REJECTED, WITHDRAWN
     * - UNDER_REVIEW -> SHORTLISTED, REJECTED, WITHDRAWN
     * - SHORTLISTED -> INTERVIEW, REJECTED, WITHDRAWN
     * - INTERVIEW -> SELECTED, REJECTED, WITHDRAWN
     * - SELECTED -> (none, final)
     * - REJECTED -> (none, final)
     * - WITHDRAWN -> (none, final)
     *
     * @param from current status
     * @return Set of allowed target statuses
     */
    public static Set<Status> getAllowedTransitions(Status from) {
        if (from == null) {
            return Collections.emptySet();
        }
        switch (from) {
            case SUBMITTED:
                return Collections.unmodifiableSet(EnumSet.of(Status.UNDER_REVIEW, Status.REJECTED, Status.WITHDRAWN));
            case UNDER_REVIEW:
                return Collections.unmodifiableSet(EnumSet.of(Status.SHORTLISTED, Status.REJECTED, Status.WITHDRAWN));
            case SHORTLISTED:
                return Collections.unmodifiableSet(EnumSet.of(Status.INTERVIEW, Status.REJECTED, Status.WITHDRAWN));
            case INTERVIEW:
                return Collections.unmodifiableSet(EnumSet.of(Status.SELECTED, Status.REJECTED, Status.WITHDRAWN));
            case SELECTED:
            case REJECTED:
            case WITHDRAWN:
            default:
                return Collections.emptySet();
        }
    }

    /**
     * Checks if transitioning from current status to the specified target status is permitted.
     *
     * @param target target status
     * @return true if transition is allowed
     */
    public boolean canTransitionTo(Status target) {
        return getAllowedTransitions(this.status).contains(target);
    }

    // Getters and Setters
    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public long getCandidateUserId() {
        return candidateUserId;
    }

    public void setCandidateUserId(long candidateUserId) {
        this.candidateUserId = candidateUserId;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    @Override
    public String toString() {
        return "JobApplication{" +
                "applicationId=" + applicationId +
                ", jobId=" + jobId +
                ", candidateUserId=" + candidateUserId +
                ", status=" + status +
                ", appliedAt=" + appliedAt +
                '}';
    }
}
