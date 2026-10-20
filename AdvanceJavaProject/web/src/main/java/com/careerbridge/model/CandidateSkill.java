package com.careerbridge.model;

import java.io.Serializable;

/**
 * Represents a skill possessed by a candidate, along with their proficiency level.
 */
public class CandidateSkill implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ProficiencyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }

    private long candidateUserId;
    private long skillId;
    private transient String skillName; // Transient for display
    private ProficiencyLevel proficiencyLevel;

    public CandidateSkill() {
        this.proficiencyLevel = ProficiencyLevel.INTERMEDIATE;
    }

    public CandidateSkill(long candidateUserId, long skillId, ProficiencyLevel proficiencyLevel) {
        this.candidateUserId = candidateUserId;
        this.skillId = skillId;
        this.proficiencyLevel = proficiencyLevel;
    }

    public CandidateSkill(long candidateUserId, long skillId, String skillName, ProficiencyLevel proficiencyLevel) {
        this.candidateUserId = candidateUserId;
        this.skillId = skillId;
        this.skillName = skillName;
        this.proficiencyLevel = proficiencyLevel;
    }

    // Getters and Setters
    public long getCandidateUserId() {
        return candidateUserId;
    }

    public void setCandidateUserId(long candidateUserId) {
        this.candidateUserId = candidateUserId;
    }

    public long getSkillId() {
        return skillId;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public ProficiencyLevel getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(ProficiencyLevel proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    @Override
    public String toString() {
        return "CandidateSkill{" +
                "candidateUserId=" + candidateUserId +
                ", skillId=" + skillId +
                ", skillName='" + skillName + '\'' +
                ", proficiencyLevel=" + proficiencyLevel +
                '}';
    }
}
