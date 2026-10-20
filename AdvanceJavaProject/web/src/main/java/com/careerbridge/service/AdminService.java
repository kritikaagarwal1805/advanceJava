package com.careerbridge.service;

import com.careerbridge.dao.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Admin dashboard and reporting service.
 */
public class AdminService {
    private final UserDAO userDAO = new UserDAO();
    private final JobDAO jobDAO = new JobDAO();
    private final ApplicationDAO applicationDAO = new ApplicationDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        try {
            stats.put("totalUsers", userDAO.count(null, null));
            stats.put("totalJobs", jobDAO.getCountsByStatus().values().stream().mapToInt(i -> i).sum());
            stats.put("totalApplications", applicationDAO.getCountsByStatus().values().stream().mapToInt(i -> i).sum());
            stats.put("totalCompanies", companyDAO.count());
            stats.put("usersByRole", userDAO.getCountsByRole());
            stats.put("jobsByStatus", jobDAO.getCountsByStatus());
            stats.put("applicationsByStatus", applicationDAO.getCountsByStatus());
        } catch (SQLException e) {
            throw new ServiceException("Failed to load dashboard stats.", e);
        }
        return stats;
    }
}
