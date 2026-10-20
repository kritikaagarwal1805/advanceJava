package com.careerbridge.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Encapsulates search and filter criteria for job listings, including pagination.
 */
public class JobSearchCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    private String keyword;
    private String location;
    private String jobType;
    private String experienceLevel;
    private String workMode;
    private Long companyId;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String status = "PUBLISHED";
    private int page = 1;
    private int pageSize = 10;

    public JobSearchCriteria() {
    }

    public JobSearchCriteria(String keyword, String location, String jobType,
                             String experienceLevel, String workMode) {
        this.keyword = keyword;
        this.location = location;
        this.jobType = jobType;
        this.experienceLevel = experienceLevel;
        this.workMode = workMode;
    }

    /**
     * Checks whether any filtering criteria (keyword, location, jobType,
     * experienceLevel, workMode, companyId, salaryMin, or salaryMax) have been specified.
     *
     * @return true if at least one filter criterion is active
     */
    public boolean hasFilters() {
        return (keyword != null && !keyword.trim().isEmpty())
                || (location != null && !location.trim().isEmpty())
                || (jobType != null && !jobType.trim().isEmpty())
                || (experienceLevel != null && !experienceLevel.trim().isEmpty())
                || (workMode != null && !workMode.trim().isEmpty())
                || companyId != null
                || salaryMin != null
                || salaryMax != null;
    }

    /**
     * Calculates the database query offset based on current page and pageSize.
     *
     * @return 0-based offset
     */
    public int getOffset() {
        int currentPage = Math.max(1, page);
        int size = Math.max(1, pageSize);
        return (currentPage - 1) * size;
    }

    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page > 0 ? page : 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize > 0 ? pageSize : 10;
    }

    @Override
    public String toString() {
        return "JobSearchCriteria{" +
                "keyword='" + keyword + '\'' +
                ", location='" + location + '\'' +
                ", jobType='" + jobType + '\'' +
                ", experienceLevel='" + experienceLevel + '\'' +
                ", workMode='" + workMode + '\'' +
                ", companyId=" + companyId +
                ", salaryMin=" + salaryMin +
                ", salaryMax=" + salaryMax +
                ", status='" + status + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
