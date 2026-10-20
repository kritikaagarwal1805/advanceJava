-- ==============================================================================
-- CareerBridge Seed Data
-- MySQL 8.0 compatible
-- Idempotent script using INSERT IGNORE / ON DUPLICATE KEY UPDATE
-- ==============================================================================

USE careerbridge;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------------------------
-- 1. Users
-- 1 Admin, 3 Employers, 5 Job Seekers
-- All demo user passwords are 'Password@123' (or 'Admin@123' for admin)
-- BCrypt Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- ------------------------------------------------------------------------------
INSERT INTO users (user_id, full_name, email, password_hash, role, status, created_at) VALUES
-- Admin
(1, 'CareerBridge Administrator', 'admin@careerbridge.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 'ACTIVE', '2026-01-01 08:00:00'),

-- Employers
(2, 'Rajesh Sharma', 'rajesh.sharma@techcorp.in', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYER', 'ACTIVE', '2026-01-05 09:30:00'),
(3, 'Priya Sundaram', 'priya.sundaram@innovatesoft.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYER', 'ACTIVE', '2026-01-06 10:15:00'),
(4, 'Vikram Malhotra', 'vikram.malhotra@dataminds.ai', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYER', 'ACTIVE', '2026-01-07 11:00:00'),

-- Job Seekers
(5, 'Aarav Patel', 'aarav.patel@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'JOB_SEEKER', 'ACTIVE', '2026-01-10 14:00:00'),
(6, 'Sneha Rao', 'sneha.rao@outlook.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'JOB_SEEKER', 'ACTIVE', '2026-01-11 15:30:00'),
(7, 'Rohan Gupta', 'rohan.gupta@yahoo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'JOB_SEEKER', 'ACTIVE', '2026-01-12 16:20:00'),
(8, 'Ananya Verma', 'ananya.verma@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'JOB_SEEKER', 'ACTIVE', '2026-01-14 12:45:00'),
(9, 'Karthik Nair', 'karthik.nair@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'JOB_SEEKER', 'ACTIVE', '2026-01-15 17:10:00')
ON DUPLICATE KEY UPDATE 
    full_name = VALUES(full_name),
    email = VALUES(email),
    password_hash = VALUES(password_hash),
    role = VALUES(role),
    status = VALUES(status);

-- ------------------------------------------------------------------------------
-- 2. Candidate Profiles
-- Profiles for Job Seekers (Users 5, 6, 7, 8, 9)
-- ------------------------------------------------------------------------------
INSERT INTO candidate_profiles (profile_id, user_id, headline, summary, phone, location, portfolio_url, linkedin_url) VALUES
(1, 5, 'Full Stack Java Developer | Spring Boot, Microservices & React', 
 'Passionate software engineer with 3+ years of experience building scalable backend services and responsive frontend applications. Specialized in Java, Spring Boot, MySQL, and React.', 
 '+91 98765 43210', 'Bengaluru, Karnataka', 'https://aaravpatel.dev', 'https://www.linkedin.com/in/aaravpatel-dev'),

(2, 6, 'Data Scientist & ML Engineer | Python, PyTorch, BigQuery', 
 'Analytical data scientist with expertise in predictive modeling, natural language processing, and scalable ML pipelines. Strong statistical foundation and hands-on deep learning experience.', 
 '+91 98765 43211', 'Hyderabad, Telangana', 'https://sneharao-ml.github.io', 'https://www.linkedin.com/in/sneharao-ds'),

(3, 7, 'Frontend Engineer & UI/UX Specialist | React, Next.js, TypeScript', 
 'User-centric frontend developer passionate about crafting accessible, high-performance web applications and sleek user interfaces with modern React ecosystems.', 
 '+91 98765 43212', 'Pune, Maharashtra', 'https://rohangupta.design', 'https://www.linkedin.com/in/rohangupta-fe'),

(4, 8, 'Recent CS Graduate | Aspiring Java & Cloud Software Engineer', 
 'Driven Computer Science honors graduate with a solid grounding in algorithms, data structures, object-oriented programming in Java, and modern web application development.', 
 '+91 98765 43213', 'New Delhi, Delhi NCR', 'https://ananyaverma.github.io', 'https://www.linkedin.com/in/ananya-verma-cs'),

(5, 9, 'Senior DevOps & Cloud Architect | AWS, Kubernetes, Terraform', 
 'Seasoned DevOps engineer with 5 years experience designing resilient CI/CD pipelines, automated cloud infrastructure provisioning, and container orchestration.', 
 '+91 98765 43214', 'Chennai, Tamil Nadu', 'https://karthiknair.cloud', 'https://www.linkedin.com/in/karthik-nair-devops')
ON DUPLICATE KEY UPDATE
    headline = VALUES(headline),
    summary = VALUES(summary),
    phone = VALUES(phone),
    location = VALUES(location),
    portfolio_url = VALUES(portfolio_url),
    linkedin_url = VALUES(linkedin_url);

-- ------------------------------------------------------------------------------
-- 3. Companies
-- 3 Distinct Employers (TechCorp India, InnovateSoft, DataMinds Analytics)
-- ------------------------------------------------------------------------------
INSERT INTO companies (company_id, owner_user_id, name, description, website, industry, location, logo_url, status, created_at) VALUES
(1, 2, 'TechCorp India', 
 'TechCorp India is a premier enterprise software and cloud consulting solutions company delivering mission-critical applications globally across banking, healthcare, and retail sectors.', 
 'https://www.techcorpindia.com', 'Information Technology', 'Bengaluru, Karnataka, India', 'https://images.unsplash.com/photo-1549923746-c502d488b3ea?w=150', 'ACTIVE', '2026-01-05 10:00:00'),

(2, 3, 'InnovateSoft', 
 'InnovateSoft develops cutting-edge SaaS platforms, modern web apps, and AI-assisted productivity tools for hyper-growth startups and established mid-market enterprises.', 
 'https://www.innovatesoft.io', 'Software & Cloud Services', 'Hyderabad, Telangana, India', 'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=150', 'ACTIVE', '2026-01-06 11:00:00'),

(3, 4, 'DataMinds Analytics', 
 'DataMinds Analytics is a pioneering data science, business intelligence, and machine learning advisory and platform engineering firm transforming raw data into actionable intelligence.', 
 'https://www.datamindsanalytics.com', 'Data & Artificial Intelligence', 'Pune, Maharashtra, India', 'https://images.unsplash.com/photo-1551836022-d5d88e9218df?w=150', 'ACTIVE', '2026-01-07 12:00:00')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description),
    website = VALUES(website),
    industry = VALUES(industry),
    location = VALUES(location),
    logo_url = VALUES(logo_url),
    status = VALUES(status);

-- ------------------------------------------------------------------------------
-- 4. Categories (6 categories)
-- ------------------------------------------------------------------------------
INSERT INTO categories (category_id, name, description) VALUES
(1, 'Technology', 'Software engineering, system architecture, database administration, and cloud infrastructure.'),
(2, 'Engineering', 'Core engineering, hardware integration, robotics, and QA automation systems.'),
(3, 'Data & Analytics', 'Data science, business intelligence, machine learning, and big data engineering.'),
(4, 'Design & Creative', 'User interface, user experience, product design, and creative digital media.'),
(5, 'Marketing & Sales', 'Digital marketing, brand growth, performance marketing, and B2B enterprise sales.'),
(6, 'Finance & Accounting', 'Corporate finance, financial analysis, accounting, and risk management.')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description);

-- ------------------------------------------------------------------------------
-- 5. Skills (20 skills)
-- ------------------------------------------------------------------------------
INSERT INTO skills (skill_id, name) VALUES
(1, 'Java'),
(2, 'Python'),
(3, 'JavaScript'),
(4, 'TypeScript'),
(5, 'React'),
(6, 'Spring Boot'),
(7, 'SQL'),
(8, 'Machine Learning'),
(9, 'Docker'),
(10, 'Kubernetes'),
(11, 'AWS'),
(12, 'HTML5/CSS3'),
(13, 'Node.js'),
(14, 'Git'),
(15, 'Data Analysis'),
(16, 'RESTful APIs'),
(17, 'UI/UX Design'),
(18, 'Microservices'),
(19, 'TensorFlow'),
(20, 'CI/CD Pipelines')
ON DUPLICATE KEY UPDATE
    name = VALUES(name);

-- ------------------------------------------------------------------------------
-- 6. Candidate Skills
-- ------------------------------------------------------------------------------
INSERT INTO candidate_skills (candidate_user_id, skill_id, proficiency_level) VALUES
-- Aarav Patel (User 5)
(5, 1, 'EXPERT'),       -- Java
(5, 6, 'ADVANCED'),     -- Spring Boot
(5, 18, 'ADVANCED'),    -- Microservices
(5, 7, 'ADVANCED'),     -- SQL
(5, 16, 'EXPERT'),      -- RESTful APIs
(5, 5, 'INTERMEDIATE'), -- React
(5, 14, 'ADVANCED'),    -- Git

-- Sneha Rao (User 6)
(6, 2, 'EXPERT'),       -- Python
(6, 8, 'ADVANCED'),     -- Machine Learning
(6, 19, 'ADVANCED'),    -- TensorFlow
(6, 15, 'EXPERT'),      -- Data Analysis
(6, 7, 'ADVANCED'),     -- SQL
(6, 14, 'INTERMEDIATE'),-- Git

-- Rohan Gupta (User 7)
(7, 5, 'EXPERT'),       -- React
(7, 3, 'EXPERT'),       -- JavaScript
(7, 4, 'ADVANCED'),     -- TypeScript
(7, 12, 'EXPERT'),      -- HTML5/CSS3
(7, 17, 'ADVANCED'),    -- UI/UX Design
(7, 14, 'ADVANCED'),    -- Git

-- Ananya Verma (User 8)
(8, 1, 'INTERMEDIATE'), -- Java
(8, 7, 'INTERMEDIATE'), -- SQL
(8, 2, 'BEGINNER'),     -- Python
(8, 12, 'INTERMEDIATE'),-- HTML5/CSS3
(8, 14, 'INTERMEDIATE'),-- Git

-- Karthik Nair (User 9)
(9, 9, 'EXPERT'),       -- Docker
(9, 10, 'EXPERT'),      -- Kubernetes
(9, 11, 'EXPERT'),      -- AWS
(9, 20, 'EXPERT'),      -- CI/CD Pipelines
(9, 2, 'INTERMEDIATE'), -- Python
(9, 14, 'EXPERT')       -- Git
ON DUPLICATE KEY UPDATE
    proficiency_level = VALUES(proficiency_level);

-- ------------------------------------------------------------------------------
-- 7. Education Records
-- ------------------------------------------------------------------------------
INSERT INTO education (education_id, candidate_user_id, institution, qualification, field_of_study, start_date, end_date, grade) VALUES
(1, 5, 'National Institute of Technology Karnataka (NITK), Surathkal', 'Bachelor of Technology (B.Tech)', 'Computer Science and Engineering', '2019-08-01', '2023-05-30', '8.8 CGPA'),
(2, 6, 'Indian Institute of Technology (IIT) Madras', 'Master of Science (M.S.)', 'Data Science & Machine Learning', '2021-08-01', '2023-06-15', '9.2 CGPA'),
(3, 6, 'Osmania University, Hyderabad', 'Bachelor of Engineering (B.E.)', 'Information Technology', '2017-08-01', '2021-05-20', '8.5 CGPA'),
(4, 7, 'Pune Institute of Computer Technology (PICT)', 'Bachelor of Engineering (B.E.)', 'Computer Engineering', '2019-08-01', '2023-05-25', '8.4 CGPA'),
(5, 8, 'Delhi Technological University (DTU)', 'Bachelor of Technology (B.Tech)', 'Software Engineering', '2022-08-01', '2026-05-30', '8.9 CGPA'),
(6, 9, 'College of Engineering, Guindy (CEG), Anna University', 'Bachelor of Engineering (B.E.)', 'Electronics and Communication', '2018-08-01', '2022-05-20', '8.7 CGPA')
ON DUPLICATE KEY UPDATE
    institution = VALUES(institution),
    qualification = VALUES(qualification),
    field_of_study = VALUES(field_of_study),
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    grade = VALUES(grade);

-- ------------------------------------------------------------------------------
-- 8. Resumes
-- ------------------------------------------------------------------------------
INSERT INTO resumes (resume_id, candidate_user_id, original_filename, stored_filename, storage_path, content_type, file_size, is_primary, uploaded_at) VALUES
(1, 5, 'Aarav_Patel_Resume.pdf', 'resume_user_5_1710000001.pdf', 'uploads/resumes/resume_user_5_1710000001.pdf', 'application/pdf', 124500, TRUE, '2026-01-10 10:00:00'),
(2, 6, 'Sneha_Rao_ML_Resume.pdf', 'resume_user_6_1710000002.pdf', 'uploads/resumes/resume_user_6_1710000002.pdf', 'application/pdf', 145200, TRUE, '2026-01-12 11:30:00'),
(3, 7, 'Rohan_Gupta_Frontend_CV.pdf', 'resume_user_7_1710000003.pdf', 'uploads/resumes/resume_user_7_1710000003.pdf', 'application/pdf', 112000, TRUE, '2026-01-15 09:15:00'),
(4, 8, 'Ananya_Verma_Academic_Resume.pdf', 'resume_user_8_1710000004.pdf', 'uploads/resumes/resume_user_8_1710000004.pdf', 'application/pdf', 98000, TRUE, '2026-01-18 14:20:00'),
(5, 9, 'Karthik_Nair_CloudDevOps_Resume.pdf', 'resume_user_9_1710000005.pdf', 'uploads/resumes/resume_user_9_1710000005.pdf', 'application/pdf', 133400, TRUE, '2026-01-20 16:45:00')
ON DUPLICATE KEY UPDATE
    original_filename = VALUES(original_filename),
    stored_filename = VALUES(stored_filename),
    storage_path = VALUES(storage_path),
    content_type = VALUES(content_type),
    file_size = VALUES(file_size),
    is_primary = VALUES(is_primary);

-- ------------------------------------------------------------------------------
-- 9. Jobs (10 diverse listings across 3 companies)
-- ------------------------------------------------------------------------------
INSERT INTO jobs (job_id, company_id, title, description, requirements, responsibilities, location, job_type, work_mode, experience_level, salary_min, salary_max, currency, application_deadline, status, created_at) VALUES
-- Job 1 (TechCorp India)
(1, 1, 'Senior Java Backend Engineer', 
 'We are seeking an experienced Senior Java Backend Engineer to design, build, and optimize high-throughput distributed microservices for our enterprise cloud platform.', 
 'Bachelor degree in Computer Science or related field. Minimum 5 years of professional backend software engineering experience in Core Java (17/21) and Spring Boot. Strong expertise in relational databases, indexing, and transactional integrity (MySQL/PostgreSQL). Hands-on familiarity with Apache Kafka, Redis caching, and Docker containerization.', 
 'Architect, develop, and maintain clean, scalable Java REST APIs and event-driven backend services. Lead code reviews and mentor junior engineering team members. Optimize database queries and system performance under peak load conditions. Collaborate cross-functionally with product managers, QA, and frontend engineering teams.', 
 'Bengaluru, Karnataka', 'FULL_TIME', 'REMOTE', 'SENIOR', 1800000.00, 2600000.00, 'INR', '2026-12-31', 'PUBLISHED', '2026-01-10 10:00:00'),

-- Job 2 (TechCorp India)
(2, 1, 'Full Stack Developer (Java & React)', 
 'Join TechCorp India as a Full Stack Developer contributing across our Java backend and responsive React frontend systems to deliver modern business solutions.', 
 '2 to 5 years experience developing web applications with Java, Spring Boot, and modern React.js. Proficiency in HTML5, CSS3, modern JavaScript/TypeScript, and state management. Solid understanding of relational database schema design and SQL.', 
 'Implement user-facing features in React and backend services in Spring Boot. Write unit and integration tests to ensure software reliability and maintainability. Troubleshoot and debug application bottlenecks across client and server tiers.', 
 'Bengaluru, Karnataka', 'FULL_TIME', 'HYBRID', 'MID', 1200000.00, 1800000.00, 'INR', '2026-11-30', 'PUBLISHED', '2026-01-12 11:00:00'),

-- Job 3 (TechCorp India)
(3, 1, 'Cloud Infrastructure & DevOps Engineer', 
 'Lead our cloud transformation initiatives, manage AWS multi-account infrastructure, automate CI/CD release cycles, and guarantee 99.99% service availability.', 
 '3+ years handling AWS infrastructure (EC2, ECS/EKS, RDS, VPC, CloudWatch). Proficiency in Terraform or CloudFormation for Infrastructure as Code (IaC). Deep hands-on knowledge of Docker, Kubernetes, and GitLab CI or GitHub Actions.', 
 'Automate build, test, and release deployment pipelines. Monitor application uptime, performance metrics, and cost optimization on cloud resources. Respond to and resolve infrastructure incidents in collaboration with on-call developers.', 
 'Bengaluru, Karnataka', 'FULL_TIME', 'ONSITE', 'SENIOR', 1600000.00, 2400000.00, 'INR', '2026-12-15', 'PUBLISHED', '2026-01-15 09:30:00'),

-- Job 4 (InnovateSoft)
(4, 2, 'Frontend React Engineer', 
 'InnovateSoft is looking for a passionate Frontend React Engineer to build intuitive, lightning-fast UI components for our next-generation collaborative SaaS application.', 
 'Strong proficiency in React 18+, TypeScript, HTML5/SCSS, and Tailwind CSS. Solid understanding of modern React hooks, component lifecycle, and state architecture. Familiarity with client-side performance profiling, accessibility standards (WCAG), and responsive UX.', 
 'Develop reusable, modular UI component libraries adhering to design guidelines. Integrate with RESTful and GraphQL APIs seamlessly. Partner with designers and product managers to refine user journeys.', 
 'Hyderabad, Telangana', 'FULL_TIME', 'REMOTE', 'MID', 1000000.00, 1600000.00, 'INR', '2026-11-20', 'PUBLISHED', '2026-01-16 14:00:00'),

-- Job 5 (InnovateSoft)
(5, 2, 'Part-Time UI/UX Designer', 
 'Design delightful and modern user interfaces for our product lineup on a flexible part-time schedule (20 hours/week).', 
 'Proficiency in Figma, Adobe XD, and wireframing tools. Portfolio demonstrating web and mobile interface designs. Strong understanding of design systems, typography, color theory, and responsive grids.', 
 'Create user flow diagrams, low-fidelity wireframes, and polished interactive prototypes. Conduct periodic user feedback sessions and translate findings into design iterations. Maintain and evolve the InnovateSoft design token system.', 
 'Remote / Work From Home', 'PART_TIME', 'REMOTE', 'MID', 400000.00, 700000.00, 'INR', '2026-11-15', 'PUBLISHED', '2026-01-18 10:00:00'),

-- Job 6 (InnovateSoft)
(6, 2, 'Software Engineering Intern', 
 'Exciting 6-month internship for enthusiastic computer science students looking to gain practical industry experience working alongside senior engineers.', 
 'Currently enrolled in or recent graduate of B.Tech/B.E./MCA in Computer Science or related degree. Good knowledge of Java or JavaScript and basic understanding of relational databases. Eagerness to learn modern frameworks and software engineering best practices.', 
 'Assist in developing features, writing automated tests, and documenting internal API endpoints. Participate in daily standups, code walkthroughs, and sprint planning meetings. Complete a capstone engineering project during the internship duration.', 
 'Hyderabad, Telangana', 'INTERNSHIP', 'HYBRID', 'ENTRY', 300000.00, 480000.00, 'INR', '2026-10-31', 'PUBLISHED', '2026-01-20 12:00:00'),

-- Job 7 (InnovateSoft)
(7, 2, 'Principal Product Manager - SaaS', 
 'Drive the strategic product vision, feature roadmap, and customer discovery initiatives for our flagship enterprise workflow tool.', 
 '6+ years in software product management with at least 3 years in B2B SaaS. Strong analytical mindset and data-informed decision-making capabilities. Superb stakeholder communication and technical empathy.', 
 'Define product roadmap, prioritize feature backlog, and write clear PRDs. Coordinate cross-functional launch efforts between Engineering, Marketing, and Sales. Monitor core product health metrics, retention, and Net Promoter Score (NPS).', 
 'Hyderabad, Telangana', 'FULL_TIME', 'ONSITE', 'EXECUTIVE', 2500000.00, 3500000.00, 'INR', '2026-12-31', 'DRAFT', '2026-01-22 15:00:00'),

-- Job 8 (DataMinds Analytics)
(8, 3, 'Lead Data Scientist', 
 'DataMinds Analytics is seeking an experienced Lead Data Scientist to head up the development of predictive analytics and NLP models for our financial intelligence client portfolio.', 
 'Master degree or Ph.D. in Computer Science, Statistics, Mathematics, or related quantitative field. 5+ years building and deploying machine learning models into production environments. Expert proficiency in Python, PyTorch/TensorFlow, scikit-learn, and advanced SQL querying. Deep knowledge of statistical modeling, hypothesis testing, and feature engineering.', 
 'Architect end-to-end machine learning workflows from data ingestion to model serving. Mentor a talented team of junior data scientists and machine learning engineers. Collaborate with executive enterprise clients to translate business challenges into quantitative data science solutions.', 
 'Pune, Maharashtra', 'FULL_TIME', 'HYBRID', 'SENIOR', 2200000.00, 3200000.00, 'INR', '2026-12-10', 'PUBLISHED', '2026-01-25 11:30:00'),

-- Job 9 (DataMinds Analytics)
(9, 3, 'Machine Learning Research Intern', 
 'A hands-on research internship providing real-world exposure to state-of-the-art machine learning models, statistical data cleaning, and AI application prototyping.', 
 'Pursuing degree in Computer Science, AI/Data Science, or Mathematics. Solid coding skills in Python and familiar with libraries like Pandas, NumPy, and Matplotlib. Basic understanding of neural networks, regression, and classification algorithms.', 
 'Perform exploratory data analysis (EDA) and data preprocessing on structured datasets. Implement baseline models and conduct comparative benchmarking experiments. Document experimental findings and present visualizations to senior research staff.', 
 'Remote', 'INTERNSHIP', 'REMOTE', 'ENTRY', 360000.00, 500000.00, 'INR', '2026-11-25', 'PUBLISHED', '2026-01-26 14:00:00'),

-- Job 10 (DataMinds Analytics)
(10, 3, 'Business Intelligence Analyst (Contract)', 
 '6-month fixed contract for a BI Analyst to build executive Tableau dashboards and optimize data reporting pipelines.', 
 '3+ years experience in Business Intelligence, data warehousing, and dashboard development. Strong SQL skills (complex joins, window functions, CTEs) and hands-on Tableau / PowerBI expertise. Ability to synthesize business KPI requirements into actionable analytical reports.', 
 'Design and publish automated executive dashboards tracking enterprise KPIs. Validate data integrity between warehouse tables and reporting views. Document dashboard metrics definitions and data lineage for client teams.', 
 'Pune, Maharashtra', 'CONTRACT', 'ONSITE', 'MID', 900000.00, 1300000.00, 'INR', '2026-10-15', 'CLOSED', '2026-01-05 09:00:00')
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    description = VALUES(description),
    requirements = VALUES(requirements),
    responsibilities = VALUES(responsibilities),
    location = VALUES(location),
    job_type = VALUES(job_type),
    work_mode = VALUES(work_mode),
    experience_level = VALUES(experience_level),
    salary_min = VALUES(salary_min),
    salary_max = VALUES(salary_max),
    application_deadline = VALUES(application_deadline),
    status = VALUES(status);

-- ------------------------------------------------------------------------------
-- 10. Job Categories
-- ------------------------------------------------------------------------------
INSERT INTO job_categories (job_id, category_id) VALUES
(1, 1), -- Senior Java Backend -> Technology
(1, 2), -- Senior Java Backend -> Engineering
(2, 1), -- Full Stack Developer -> Technology
(2, 2), -- Full Stack Developer -> Engineering
(3, 1), -- Cloud DevOps -> Technology
(3, 2), -- Cloud DevOps -> Engineering
(4, 1), -- Frontend React -> Technology
(4, 4), -- Frontend React -> Design & Creative
(5, 4), -- UI/UX Designer -> Design & Creative
(6, 1), -- SE Intern -> Technology
(7, 1), -- Product Manager -> Technology
(7, 5), -- Product Manager -> Marketing & Sales
(8, 1), -- Lead Data Scientist -> Technology
(8, 3), -- Lead Data Scientist -> Data & Analytics
(9, 1), -- ML Intern -> Technology
(9, 3), -- ML Intern -> Data & Analytics
(10, 3),-- BI Analyst -> Data & Analytics
(10, 6) -- BI Analyst -> Finance & Accounting
ON DUPLICATE KEY UPDATE job_id = VALUES(job_id);

-- ------------------------------------------------------------------------------
-- 11. Applications (9 diverse sample applications)
-- ------------------------------------------------------------------------------
INSERT INTO applications (application_id, job_id, candidate_user_id, resume_id, cover_letter, status, applied_at) VALUES
-- Aarav Patel (User 5)
(1, 1, 5, 1, 
 'I have spent the last 3+ years engineering high-throughput Java microservices using Spring Boot and MySQL. I am thrilled by TechCorp India mission and believe my technical skill set and passion for clean architecture make me a strong match.', 
 'SHORTLISTED', '2026-09-15 10:30:00'),

(2, 2, 5, 1, 
 'Having built end-to-end web applications combining Spring Boot REST APIs with responsive React frontends, I would love the opportunity to contribute across both tiers at TechCorp India.', 
 'INTERVIEW', '2026-09-18 14:00:00'),

-- Sneha Rao (User 6)
(3, 8, 6, 2, 
 'With an M.S. in Data Science from IIT Madras and extensive experience building deep learning pipelines with PyTorch, I am excited about the Lead Data Scientist role at DataMinds Analytics.', 
 'UNDER_REVIEW', '2026-09-20 09:45:00'),

(4, 9, 6, 2, 
 'I am passionate about cutting-edge research in statistical modeling and machine learning. I would welcome the opportunity to benchmark models and collaborate with DataMinds Analytics research scientists.', 
 'SELECTED', '2026-09-10 11:15:00'),

-- Rohan Gupta (User 7)
(5, 4, 7, 3, 
 'As an experienced React and TypeScript developer dedicated to building accessible and performant interfaces, I am eager to help InnovateSoft create exceptional user experiences.', 
 'SUBMITTED', '2026-09-22 16:30:00'),

(6, 5, 7, 3, 
 'I combine strong visual design instincts with frontend development acumen in Figma and CSS. I look forward to creating polished, user-friendly prototypes for InnovateSoft.', 
 'INTERVIEW', '2026-09-21 15:20:00'),

-- Ananya Verma (User 8)
(7, 6, 8, 4, 
 'As a top-performing computer science student with hands-on Java coursework and practical web projects, I am eager to contribute energy and learn rapidly as an intern at InnovateSoft.', 
 'SHORTLISTED', '2026-09-12 12:00:00'),

(8, 9, 8, 4, 
 'I have completed coursework in algorithms, Python, and data structures. I would love the chance to develop foundational skills in data science at DataMinds Analytics.', 
 'SUBMITTED', '2026-09-23 18:00:00'),

-- Karthik Nair (User 9)
(9, 3, 9, 5, 
 'Over the last 4+ years, I have architected automated AWS environments with Terraform, managed Kubernetes clusters, and maintained robust CI/CD pipelines. I would love to support TechCorp cloud scale.', 
 'SELECTED', '2026-09-05 13:40:00')
ON DUPLICATE KEY UPDATE
    resume_id = VALUES(resume_id),
    cover_letter = VALUES(cover_letter),
    status = VALUES(status);

-- ------------------------------------------------------------------------------
-- 12. Application Status History
-- ------------------------------------------------------------------------------
INSERT INTO application_status_history (history_id, application_id, old_status, new_status, changed_by_user_id, notes, changed_at) VALUES
-- History for App 1 (Aarav -> Job 1)
(1, 1, 'SUBMITTED', 'UNDER_REVIEW', 2, 'Resume meets senior engineering qualifications.', '2026-09-16 11:00:00'),
(2, 1, 'UNDER_REVIEW', 'SHORTLISTED', 2, 'Passed initial screening; shortlisted for technical evaluation.', '2026-09-18 10:15:00'),

-- History for App 2 (Aarav -> Job 2)
(3, 2, 'SUBMITTED', 'UNDER_REVIEW', 2, 'Application under review by hiring manager.', '2026-09-19 14:00:00'),
(4, 2, 'UNDER_REVIEW', 'SHORTLISTED', 2, 'Candidate profile approved for first round.', '2026-09-21 16:30:00'),
(5, 2, 'SHORTLISTED', 'INTERVIEW', 2, 'Technical interview scheduled with Lead Architect.', '2026-09-24 10:00:00'),

-- History for App 4 (Sneha -> Job 9)
(6, 4, 'SUBMITTED', 'UNDER_REVIEW', 4, 'Profile received and assigned for review.', '2026-09-11 09:30:00'),
(7, 4, 'UNDER_REVIEW', 'SHORTLISTED', 4, 'Excellent academic record and project portfolio.', '2026-09-13 14:20:00'),
(8, 4, 'SHORTLISTED', 'INTERVIEW', 4, 'Interview conducted with senior data scientist.', '2026-09-17 11:00:00'),
(9, 4, 'INTERVIEW', 'SELECTED', 4, 'Offer extended for the ML Research Internship role.', '2026-09-20 15:45:00'),

-- History for App 6 (Rohan -> Job 5)
(10, 6, 'SUBMITTED', 'UNDER_REVIEW', 3, 'Portfolio links verified.', '2026-09-22 10:00:00'),
(11, 6, 'UNDER_REVIEW', 'SHORTLISTED', 3, 'Design portfolio is impressive.', '2026-09-23 11:30:00'),
(12, 6, 'SHORTLISTED', 'INTERVIEW', 3, 'Design walkthrough interview scheduled.', '2026-09-25 14:00:00'),

-- History for App 7 (Ananya -> Job 6)
(13, 7, 'SUBMITTED', 'UNDER_REVIEW', 3, 'Academic transcripts verified.', '2026-09-13 15:00:00'),
(14, 7, 'UNDER_REVIEW', 'SHORTLISTED', 3, 'Selected for technical aptitude round.', '2026-09-15 16:00:00'),

-- History for App 9 (Karthik -> Job 3)
(15, 9, 'SUBMITTED', 'UNDER_REVIEW', 2, 'Strong DevOps background on AWS & K8s.', '2026-09-06 10:00:00'),
(16, 9, 'UNDER_REVIEW', 'SHORTLISTED', 2, 'Selected for technical interview.', '2026-09-08 14:00:00'),
(17, 9, 'SHORTLISTED', 'INTERVIEW', 2, 'Hands-on practical session completed with high marks.', '2026-09-12 11:30:00'),
(18, 9, 'INTERVIEW', 'SELECTED', 2, 'Offer letter issued and accepted.', '2026-09-15 17:00:00')
ON DUPLICATE KEY UPDATE
    old_status = VALUES(old_status),
    new_status = VALUES(new_status),
    notes = VALUES(notes);

SET FOREIGN_KEY_CHECKS = 1;
