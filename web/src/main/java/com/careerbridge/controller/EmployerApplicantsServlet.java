package com.careerbridge.controller;

import com.careerbridge.model.Job;
import com.careerbridge.model.JobApplication;
import com.careerbridge.model.User;
import com.careerbridge.service.ApplicationService;
import com.careerbridge.service.JobService;
import com.careerbridge.util.CsrfUtil;
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

@WebServlet("/employer/applicants")
public class EmployerApplicantsServlet extends HttpServlet {
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

        String jobIdStr = req.getParameter("jobId");
        if (jobIdStr == null || jobIdStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/employer/jobs");
            return;
        }

        try {
            long jobId = Long.parseLong(jobIdStr);
            if (!jobService.isJobOwnedByUser(jobId, user.getUserId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized access to job applicants");
                return;
            }

            Optional<Job> jobOpt = jobService.getJobById(jobId);
            jobOpt.ifPresent(j -> req.setAttribute("job", j));

            String statusFilter = req.getParameter("status");
            List<JobApplication> applicants = applicationService.getApplicationsByJob(jobId, statusFilter);
            req.setAttribute("applicants", applicants);
            req.setAttribute("statusFilter", statusFilter);

            Map<String, Integer> stats = applicationService.getApplicationCountsForJob(jobId);
            req.setAttribute("statusStats", stats);
            req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));

            req.getRequestDispatcher("/employer/applicants.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/employer/jobs");
        }
    }
}
