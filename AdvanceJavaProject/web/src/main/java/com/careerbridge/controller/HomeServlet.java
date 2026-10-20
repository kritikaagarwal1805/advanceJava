package com.careerbridge.controller;

import com.careerbridge.model.*;
import com.careerbridge.service.CompanyService;
import com.careerbridge.service.JobService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("")
public class HomeServlet extends HttpServlet {
    private final JobService jobService = new JobService();
    private final CompanyService companyService = new CompanyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Job> recentJobs = jobService.getRecentJobs(6);
            req.setAttribute("recentJobs", recentJobs);
            req.setAttribute("totalJobs", jobService.getJobCountsByStatus().values().stream().mapToInt(i -> i).sum());
            req.setAttribute("totalCompanies", companyService.getCompanyCount());
        } catch (Exception e) {
            req.setAttribute("recentJobs", List.of());
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
