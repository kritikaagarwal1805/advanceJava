package com.careerbridge.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Remote object implementation of JobSearchService.
 * Extends UnicastRemoteObject to enable remote export over TCP.
 *
 * RTU Syllabus: RMI Servant Implementation, UnicastRemoteObject,
 * Remote Object Export, Thread-safe In-Memory State.
 */
public class JobSearchServiceImpl extends UnicastRemoteObject implements JobSearchService {

    private static final long serialVersionUID = 1L;

    // Internal record to hold job data
    private static class JobEntry {
        final int id;
        final String title;
        final String company;
        final String location;
        final String type;
        final String status;
        final String salary;
        final String description;

        JobEntry(int id, String title, String company, String location,
                 String type, String status, String salary, String description) {
            this.id = id;
            this.title = title;
            this.company = company;
            this.location = location;
            this.type = type;
            this.status = status;
            this.salary = salary;
            this.description = description;
        }
    }

    private final Map<Integer, JobEntry> jobDatabase = new LinkedHashMap<>();

    public JobSearchServiceImpl() throws RemoteException {
        super();
        initSampleData();
    }

    public JobSearchServiceImpl(int port) throws RemoteException {
        super(port);
        initSampleData();
    }

    private void initSampleData() {
        jobDatabase.put(101, new JobEntry(101, "Lead Java Architect", "Oracle India", "Bengaluru",
                "Full-time", "PUBLISHED", "INR 35-45 LPA",
                "Architect enterprise cloud microservices with Spring Boot, Jakarta EE & Kubernetes."));

        jobDatabase.put(102, new JobEntry(102, "Full Stack Web Developer", "TCS Digital", "Jaipur",
                "Full-time", "PUBLISHED", "INR 12-18 LPA",
                "Develop responsive web interfaces with HTML5/CSS3/React and RESTful Java backend APIs."));

        jobDatabase.put(103, new JobEntry(103, "Cloud Infrastructure Engineer", "Amazon AWS", "Hyderabad",
                "Full-time", "PUBLISHED", "INR 24-32 LPA",
                "Design scalable AWS VPC networks, Terraform provisioning, and automated CI/CD pipelines."));

        jobDatabase.put(104, new JobEntry(104, "Junior Software Intern", "Infosys", "Pune",
                "Internship", "PUBLISHED", "INR 30,000/mo",
                "Gain hands-on training with Java core concepts, JDBC, servlets, and automated unit testing."));

        jobDatabase.put(105, new JobEntry(105, "Data Analyst / BI Specialist", "Wipro", "Noida",
                "Full-time", "DRAFT", "INR 10-14 LPA",
                "Analyze recruitment metrics, design PowerBI dashboards, and execute SQL analytics queries."));

        jobDatabase.put(106, new JobEntry(106, "Senior QA Automation Engineer", "HCL Tech", "Chennai",
                "Full-time", "CLOSED", "INR 16-22 LPA",
                "Build Selenium and JUnit automated test suites for continuous integration testing."));
    }

    @Override
    public List<String> searchJobs(String keyword) throws RemoteException {
        List<String> results = new ArrayList<>();
        String query = (keyword != null) ? keyword.trim().toLowerCase() : "";

        for (JobEntry job : jobDatabase.values()) {
            if (query.isEmpty()
                    || job.title.toLowerCase().contains(query)
                    || job.company.toLowerCase().contains(query)
                    || job.location.toLowerCase().contains(query)
                    || job.type.toLowerCase().contains(query)
                    || job.description.toLowerCase().contains(query)) {

                results.add(String.format("[%d] %s at %s (%s) - %s [%s]",
                        job.id, job.title, job.company, job.location, job.type, job.status));
            }
        }
        return results;
    }

    @Override
    public String getJobDetails(int jobId) throws RemoteException {
        JobEntry job = jobDatabase.get(jobId);
        if (job == null) {
            return "ERROR: Job ID " + jobId + " not found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append(String.format("Job ID:       %d%n", job.id));
        sb.append(String.format("Title:        %s%n", job.title));
        sb.append(String.format("Company:      %s%n", job.company));
        sb.append(String.format("Location:     %s%n", job.location));
        sb.append(String.format("Type:         %s%n", job.type));
        sb.append(String.format("Status:       %s%n", job.status));
        sb.append(String.format("Salary:       %s%n", job.salary));
        sb.append(String.format("Description:  %s%n", job.description));
        sb.append("==================================================");
        return sb.toString();
    }

    @Override
    public Map<String, Integer> getStatistics() throws RemoteException {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("TOTAL", jobDatabase.size());
        stats.put("PUBLISHED", 0);
        stats.put("DRAFT", 0);
        stats.put("CLOSED", 0);

        for (JobEntry job : jobDatabase.values()) {
            String status = job.status.toUpperCase();
            stats.put(status, stats.getOrDefault(status, 0) + 1);
        }
        return stats;
    }
}
