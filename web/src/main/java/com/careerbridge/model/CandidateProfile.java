package com.careerbridge.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a candidate's profile in the CareerBridge system.
 */
public class CandidateProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private long profileId;
    private long userId;
    private String headline;
    private String summary;
    private String phone;
    private String location;
    private String portfolioUrl;
    private String linkedinUrl;
    private LocalDateTime updatedAt;

    public CandidateProfile() {
    }

    public CandidateProfile(long userId, String headline, String summary, String phone,
                            String location, String portfolioUrl, String linkedinUrl) {
        this.userId = userId;
        this.headline = headline;
        this.summary = summary;
        this.phone = phone;
        this.location = location;
        this.portfolioUrl = portfolioUrl;
        this.linkedinUrl = linkedinUrl;
    }

    public CandidateProfile(long profileId, long userId, String headline, String summary,
                            String phone, String location, String portfolioUrl,
                            String linkedinUrl, LocalDateTime updatedAt) {
        this.profileId = profileId;
        this.userId = userId;
        this.headline = headline;
        this.summary = summary;
        this.phone = phone;
        this.location = location;
        this.portfolioUrl = portfolioUrl;
        this.linkedinUrl = linkedinUrl;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "CandidateProfile{" +
                "profileId=" + profileId +
                ", userId=" + userId +
                ", headline='" + headline + '\'' +
                ", phone='" + phone + '\'' +
                ", location='" + location + '\'' +
                ", portfolioUrl='" + portfolioUrl + '\'' +
                ", linkedinUrl='" + linkedinUrl + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
