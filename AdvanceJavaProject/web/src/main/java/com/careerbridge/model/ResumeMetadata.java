package com.careerbridge.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Represents metadata for a candidate's uploaded resume file.
 */
public class ResumeMetadata implements Serializable {
    private static final long serialVersionUID = 1L;

    private long resumeId;
    private long candidateUserId;
    private String originalFilename;
    private String storedFilename;
    private String storagePath;
    private String contentType;
    private long fileSize;
    private boolean isPrimary;
    private LocalDateTime uploadedAt;

    public ResumeMetadata() {
    }

    public ResumeMetadata(long candidateUserId, String originalFilename, String storedFilename,
                          String storagePath, String contentType, long fileSize, boolean isPrimary) {
        this.candidateUserId = candidateUserId;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.storagePath = storagePath;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.isPrimary = isPrimary;
    }

    public ResumeMetadata(long resumeId, long candidateUserId, String originalFilename,
                          String storedFilename, String storagePath, String contentType,
                          long fileSize, boolean isPrimary, LocalDateTime uploadedAt) {
        this.resumeId = resumeId;
        this.candidateUserId = candidateUserId;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.storagePath = storagePath;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.isPrimary = isPrimary;
        this.uploadedAt = uploadedAt;
    }

    /**
     * Formats the file size in bytes to a human-readable string (e.g. "512 B", "2.4 MB").
     *
     * @return human-readable file size string
     */
    public String getFileSizeFormatted() {
        if (fileSize <= 0) {
            return "0 B";
        }
        if (fileSize < 1024) {
            return fileSize + " B";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(fileSize) / Math.log10(1024));
        if (digitGroups >= units.length) {
            digitGroups = units.length - 1;
        }
        return String.format(Locale.US, "%.1f %s", fileSize / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    // Getters and Setters
    public long getResumeId() {
        return resumeId;
    }

    public void setResumeId(long resumeId) {
        this.resumeId = resumeId;
    }

    public long getCandidateUserId() {
        return candidateUserId;
    }

    public void setCandidateUserId(long candidateUserId) {
        this.candidateUserId = candidateUserId;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        this.isPrimary = primary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String toString() {
        return "ResumeMetadata{" +
                "resumeId=" + resumeId +
                ", candidateUserId=" + candidateUserId +
                ", originalFilename='" + originalFilename + '\'' +
                ", contentType='" + contentType + '\'' +
                ", fileSize=" + getFileSizeFormatted() +
                ", isPrimary=" + isPrimary +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
