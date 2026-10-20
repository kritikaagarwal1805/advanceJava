package com.careerbridge.network;

import java.io.Serializable;

/**
 * Serializable Data Transfer Object (DTO) representing a high-level job summary.
 * Used for network transmission and object serialization demos.
 *
 * RTU Syllabus: Java Object Serialization, Serializable Interface, serialVersionUID.
 */
public class JobSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String title;
    private String company;
    private String location;
    private String type;
    private String status;

    public JobSummary() {
    }

    public JobSummary(long id, String title, String company, String location, String type, String status) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.type = type;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s at %s (%s) | %s | %s",
                id, title, company, location, type, status);
    }
}
