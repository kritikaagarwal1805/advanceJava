package com.careerbridge.controller;

import com.careerbridge.model.Job;
import com.careerbridge.model.JobSearchCriteria;
import com.careerbridge.model.User;
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

@WebServlet("/admin/jobs")
public class AdminJobsServlet extends HttpServlet {
    private final JobService jobService = new JobService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        JobSearchCriteria criteria = new JobSearchCriteria();
        criteria.setKeyword(req.getParameter("keyword"));
        criteria.setStatus(req.getParameter("status"));

        int page = 1;
        try {
            String p = req.getParameter("page");
            if (p != null) page = Math.max(1, Integer.parseInt(p));
        } catch (NumberFormatException ignored) {}
        criteria.setPage(page);
        criteria.setPageSize(10);

        List<Job> jobs = jobService.searchJobs(criteria);
        int totalResults = jobService.countSearchResults(criteria);
        int totalPages = (int) Math.ceil((double) totalResults / criteria.getPageSize());

        req.setAttribute("jobs", jobs);
        req.setAttribute("criteria", criteria);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));

        req.getRequestDispatcher("/admin/jobs.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String jobIdStr = req.getParameter("jobId");
        String action = req.getParameter("action");

        try {
            long jobId = Long.parseLong(jobIdStr);
            Job.Status status = "activate".equalsIgnoreCase(action) ? Job.Status.PUBLISHED : Job.Status.DEACTIVATED;
            // Admin override
            var jobOpt = jobService.getJobById(jobId);
            if (jobOpt.isPresent()) {
                Job job = jobOpt.get();
                job.setStatus(status);
                new com.careerbridge.dao.JobDAO().updateStatus(jobId, status);
                req.getSession().setAttribute("success", "Job status updated to " + status);
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Failed to update job: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/jobs");
    }
}
