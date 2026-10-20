package com.careerbridge.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a skill definition in the CareerBridge system.
 */
public class Skill implements Serializable {
    private static final long serialVersionUID = 1L;

    private long skillId;
    private String name;

    public Skill() {
    }

    public Skill(String name) {
        this.name = name;
    }

    public Skill(long skillId, String name) {
        this.skillId = skillId;
        this.name = name;
    }

    // Getters and Setters
    public long getSkillId() {
        return skillId;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return skillId == skill.skillId && Objects.equals(name, skill.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillId, name);
    }

    @Override
    public String toString() {
        return "Skill{" +
                "skillId=" + skillId +
                ", name='" + name + '\'' +
                '}';
    }
}
