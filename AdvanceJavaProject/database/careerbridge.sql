-- ==============================================================================
-- CareerBridge Database Schema
-- MySQL 8.0 compatible
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS careerbridge DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE careerbridge;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------------------------
-- Table 1: users
-- Stores authentication and base user identity across roles
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('JOB_SEEKER', 'EMPLOYER', 'ADMIN') NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email),
    INDEX idx_users_role (role),
    INDEX idx_users_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 2: candidate_profiles
-- Extends users table with job seeker professional information
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS candidate_profiles;
CREATE TABLE IF NOT EXISTS candidate_profiles (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    headline VARCHAR(200) NULL,
    summary TEXT NULL,
    phone VARCHAR(20) NULL,
    location VARCHAR(100) NULL,
    portfolio_url VARCHAR(500) NULL,
    linkedin_url VARCHAR(500) NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_candidate_profiles_user_id (user_id),
    CONSTRAINT fk_candidate_profiles_user FOREIGN KEY (user_id) 
        REFERENCES users (user_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 3: companies
-- Employer business profiles
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS companies;
CREATE TABLE IF NOT EXISTS companies (
    company_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_user_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT NULL,
    website VARCHAR(500) NULL,
    industry VARCHAR(100) NULL,
    location VARCHAR(200) NULL,
    logo_url VARCHAR(500) NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'PENDING') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_companies_owner_user_id (owner_user_id),
    INDEX idx_companies_status (status),
    CONSTRAINT fk_companies_owner FOREIGN KEY (owner_user_id) 
        REFERENCES users (user_id) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 4: jobs
-- Job vacancy postings managed by employer companies
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS jobs;
CREATE TABLE IF NOT EXISTS jobs (
    job_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    requirements TEXT NULL,
    responsibilities TEXT NULL,
    location VARCHAR(200) NULL,
    job_type ENUM('FULL_TIME', 'PART_TIME', 'INTERNSHIP', 'CONTRACT') NOT NULL,
    work_mode ENUM('ONSITE', 'REMOTE', 'HYBRID') DEFAULT 'ONSITE',
    experience_level ENUM('ENTRY', 'MID', 'SENIOR', 'EXECUTIVE') DEFAULT 'ENTRY',
    salary_min DECIMAL(12,2) NULL,
    salary_max DECIMAL(12,2) NULL,
    currency VARCHAR(3) DEFAULT 'INR',
    application_deadline DATE NULL,
    status ENUM('DRAFT', 'PUBLISHED', 'CLOSED', 'DEACTIVATED') NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_jobs_company_id (company_id),
    INDEX idx_jobs_status (status),
    INDEX idx_jobs_job_type (job_type),
    INDEX idx_jobs_experience_level (experience_level),
    INDEX idx_jobs_location (location),
    FULLTEXT INDEX idx_jobs_title_desc (title, description),
    CONSTRAINT fk_jobs_company FOREIGN KEY (company_id) 
        REFERENCES companies (company_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 5: resumes
-- Uploaded resume documents metadata for job seekers
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS resumes;
CREATE TABLE IF NOT EXISTS resumes (
    resume_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_user_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_resumes_candidate_user_id (candidate_user_id),
    CONSTRAINT fk_resumes_candidate FOREIGN KEY (candidate_user_id) 
        REFERENCES users (user_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 6: applications
-- Job applications submitted by job seekers
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS applications;
CREATE TABLE IF NOT EXISTS applications (
    application_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    candidate_user_id BIGINT NOT NULL,
    resume_id BIGINT NULL,
    cover_letter TEXT NULL,
    status ENUM('SUBMITTED', 'UNDER_REVIEW', 'SHORTLISTED', 'INTERVIEW', 'SELECTED', 'REJECTED', 'WITHDRAWN') NOT NULL DEFAULT 'SUBMITTED',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_applications_job_candidate (job_id, candidate_user_id),
    INDEX idx_applications_job_id (job_id),
    INDEX idx_applications_candidate_user_id (candidate_user_id),
    INDEX idx_applications_status (status),
    INDEX idx_applications_resume_id (resume_id),
    CONSTRAINT fk_applications_job FOREIGN KEY (job_id) 
        REFERENCES jobs (job_id) 
        ON DELETE RESTRICT 
        ON UPDATE CASCADE,
    CONSTRAINT fk_applications_candidate FOREIGN KEY (candidate_user_id) 
        REFERENCES users (user_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT fk_applications_resume FOREIGN KEY (resume_id) 
        REFERENCES resumes (resume_id) 
        ON DELETE SET NULL 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 7: application_status_history
-- Audit log of status transitions for job applications
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS application_status_history;
CREATE TABLE IF NOT EXISTS application_status_history (
    history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT NOT NULL,
    old_status VARCHAR(20) NULL,
    new_status VARCHAR(20) NOT NULL,
    changed_by_user_id BIGINT NULL,
    notes TEXT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_ash_application_id (application_id),
    INDEX idx_ash_changed_by_user_id (changed_by_user_id),
    CONSTRAINT fk_ash_application FOREIGN KEY (application_id) 
        REFERENCES applications (application_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT fk_ash_changed_by FOREIGN KEY (changed_by_user_id) 
        REFERENCES users (user_id) 
        ON DELETE SET NULL 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 8: education
-- Academic history for job seekers
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS education;
CREATE TABLE IF NOT EXISTS education (
    education_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_user_id BIGINT NOT NULL,
    institution VARCHAR(200) NOT NULL,
    qualification VARCHAR(100) NOT NULL,
    field_of_study VARCHAR(100) NULL,
    start_date DATE NULL,
    end_date DATE NULL,
    grade VARCHAR(20) NULL,
    INDEX idx_education_candidate_user_id (candidate_user_id),
    CONSTRAINT fk_education_candidate FOREIGN KEY (candidate_user_id) 
        REFERENCES users (user_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 9: skills
-- Global skills dictionary
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS skills;
CREATE TABLE IF NOT EXISTS skills (
    skill_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 10: candidate_skills
-- Many-to-many relationship mapping skills to candidates with proficiency
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS candidate_skills;
CREATE TABLE IF NOT EXISTS candidate_skills (
    candidate_user_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    proficiency_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT') DEFAULT 'INTERMEDIATE',
    PRIMARY KEY (candidate_user_id, skill_id),
    INDEX idx_candidate_skills_skill_id (skill_id),
    CONSTRAINT fk_candidate_skills_candidate FOREIGN KEY (candidate_user_id) 
        REFERENCES users (user_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT fk_candidate_skills_skill FOREIGN KEY (skill_id) 
        REFERENCES skills (skill_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 11: categories
-- Occupational categories for job classification
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS categories;
CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------------------------
-- Table 12: job_categories
-- Many-to-many relationship mapping jobs to categories
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS job_categories;
CREATE TABLE IF NOT EXISTS job_categories (
    job_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (job_id, category_id),
    INDEX idx_job_categories_category_id (category_id),
    CONSTRAINT fk_job_categories_job FOREIGN KEY (job_id) 
        REFERENCES jobs (job_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT fk_job_categories_category FOREIGN KEY (category_id) 
        REFERENCES categories (category_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
