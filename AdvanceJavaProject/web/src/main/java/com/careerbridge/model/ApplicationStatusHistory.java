package com.careerbridge.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Tracks status transitions and auditing history for a job application.
 */
public class ApplicationStatusHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private long historyId;
    private long applicationId;
    private String oldStatus;
    private String newStatus;
    private long changedByUserId;
    private String notes;
    private LocalDateTime changedAt;

    public ApplicationStatusHistory() {
    }

    public ApplicationStatusHistory(long applicationId, String oldStatus, String newStatus,
                                    long changedByUserId, String notes) {
        this.applicationId = applicationId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedByUserId = changedByUserId;
        this.notes = notes;
    }

    public ApplicationStatusHistory(long historyId, long applicationId, String oldStatus,
                                    String newStatus, long changedByUserId, String notes,
                                    LocalDateTime changedAt) {
        this.historyId = historyId;
        this.applicationId = applicationId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedByUserId = changedByUserId;
        this.notes = notes;
        this.changedAt = changedAt;
    }

    // Getters and Setters
    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public long getChangedByUserId() {
        return changedByUserId;
    }

    public void setChangedByUserId(long changedByUserId) {
        this.changedByUserId = changedByUserId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    @Override
    public String toString() {
        return "ApplicationStatusHistory{" +
                "historyId=" + historyId +
                ", applicationId=" + applicationId +
                ", oldStatus='" + oldStatus + '\'' +
                ", newStatus='" + newStatus + '\'' +
                ", changedByUserId=" + changedByUserId +
                ", notes='" + notes + '\'' +
                ", changedAt=" + changedAt +
                '}';
    }
}
