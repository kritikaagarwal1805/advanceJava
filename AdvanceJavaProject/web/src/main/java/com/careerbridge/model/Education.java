package com.careerbridge.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents an educational record for a candidate.
 */
public class Education implements Serializable {
    private static final long serialVersionUID = 1L;

    private long educationId;
    private long candidateUserId;
    private String institution;
    private String qualification;
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private String grade;

    public Education() {
    }

    public Education(long candidateUserId, String institution, String qualification,
                     String fieldOfStudy, LocalDate startDate, LocalDate endDate, String grade) {
        this.candidateUserId = candidateUserId;
        this.institution = institution;
        this.qualification = qualification;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
    }

    public Education(long educationId, long candidateUserId, String institution,
                     String qualification, String fieldOfStudy, LocalDate startDate,
                     LocalDate endDate, String grade) {
        this.educationId = educationId;
        this.candidateUserId = candidateUserId;
        this.institution = institution;
        this.qualification = qualification;
        this.fieldOfStudy = fieldOfStudy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.grade = grade;
    }

    // Getters and Setters
    public long getEducationId() {
        return educationId;
    }

    public void setEducationId(long educationId) {
        this.educationId = educationId;
    }

    public long getCandidateUserId() {
        return candidateUserId;
    }

    public void setCandidateUserId(long candidateUserId) {
        this.candidateUserId = candidateUserId;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Education{" +
                "educationId=" + educationId +
                ", candidateUserId=" + candidateUserId +
                ", institution='" + institution + '\'' +
                ", qualification='" + qualification + '\'' +
                ", fieldOfStudy='" + fieldOfStudy + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", grade='" + grade + '\'' +
                '}';
    }
}
