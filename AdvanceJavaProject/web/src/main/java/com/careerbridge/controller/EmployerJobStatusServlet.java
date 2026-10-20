package com.careerbridge.controller;

import com.careerbridge.model.Job;
import com.careerbridge.model.User;
import com.careerbridge.service.JobService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/employer/job-status")
public class EmployerJobStatusServlet extends HttpServlet {
    private final JobService jobService = new JobService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String jobIdStr = req.getParameter("jobId");
        String statusStr = req.getParameter("status");

        try {
            long jobId = Long.parseLong(jobIdStr);
            Job.Status status = Job.Status.valueOf(statusStr);
            jobService.updateJobStatus(jobId, status, user.getUserId());
            req.getSession().setAttribute("success", "Job status updated to " + status);
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Failed to update status: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/employer/jobs");
    }
}
