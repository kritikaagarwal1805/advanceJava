package com.careerbridge.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents an employer's company in the CareerBridge system.
 */
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status {
        ACTIVE, INACTIVE, PENDING
    }

    private long companyId;
    private long ownerUserId;
    private String name;
    private String description;
    private String website;
    private String industry;
    private String location;
    private String logoUrl;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Company() {
        this.status = Status.PENDING;
    }

    public Company(long ownerUserId, String name, String description, String website,
                   String industry, String location, String logoUrl) {
        this.ownerUserId = ownerUserId;
        this.name = name;
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.location = location;
        this.logoUrl = logoUrl;
        this.status = Status.PENDING;
    }

    public Company(long companyId, long ownerUserId, String name, String description,
                   String website, String industry, String location, String logoUrl,
                   Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.companyId = companyId;
        this.ownerUserId = ownerUserId;
        this.name = name;
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.location = location;
        this.logoUrl = logoUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", ownerUserId=" + ownerUserId +
                ", name='" + name + '\'' +
                ", industry='" + industry + '\'' +
                ", location='" + location + '\'' +
                ", status=" + status +
                '}';
    }
}
