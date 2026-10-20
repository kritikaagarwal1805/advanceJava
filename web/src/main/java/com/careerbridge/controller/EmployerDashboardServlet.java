package com.careerbridge.controller;

import com.careerbridge.model.Company;
import com.careerbridge.model.Job;
import com.careerbridge.model.User;
import com.careerbridge.service.ApplicationService;
import com.careerbridge.service.CompanyService;
import com.careerbridge.service.JobService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/employer/dashboard")
public class EmployerDashboardServlet extends HttpServlet {
    private final CompanyService companyService = new CompanyService();
    private final JobService jobService = new JobService();
    private final ApplicationService applicationService = new ApplicationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Optional<Company> companyOpt = companyService.getCompanyByOwner(user.getUserId());
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            req.setAttribute("company", company);

            List<Job> jobs = jobService.getJobsByOwner(user.getUserId());
            req.setAttribute("jobs", jobs);
            req.setAttribute("totalJobs", jobs.size());

            long activeJobs = jobs.stream().filter(j -> j.getStatus() == Job.Status.PUBLISHED).count();
            req.setAttribute("activeJobs", activeJobs);

            Map<String, Integer> appStats = applicationService.getApplicationCountsForEmployer(user.getUserId());
            req.setAttribute("appStats", appStats);

            int totalApplicants = appStats.values().stream().mapToInt(Integer::intValue).sum();
            req.setAttribute("totalApplicants", totalApplicants);
            req.setAttribute("pendingReviews", appStats.getOrDefault("SUBMITTED", 0));
        }

        req.getRequestDispatcher("/employer/dashboard.jsp").forward(req, resp);
    }
}
