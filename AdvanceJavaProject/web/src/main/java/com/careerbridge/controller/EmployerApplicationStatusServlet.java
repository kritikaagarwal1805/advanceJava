package com.careerbridge.controller;

import com.careerbridge.model.JobApplication;
import com.careerbridge.model.User;
import com.careerbridge.service.ApplicationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/employer/application-status")
public class EmployerApplicationStatusServlet extends HttpServlet {
    private final ApplicationService applicationService = new ApplicationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String appIdStr = req.getParameter("applicationId");
        String newStatusStr = req.getParameter("newStatus");
        String notes = req.getParameter("notes");
        String redirectJobId = req.getParameter("jobId");

        try {
            long applicationId = Long.parseLong(appIdStr);
            if (!applicationService.isApplicationAccessibleByEmployer(applicationId, user.getUserId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized application access");
                return;
            }

            JobApplication.Status newStatus = JobApplication.Status.valueOf(newStatusStr);
            applicationService.updateStatus(applicationId, newStatus, user.getUserId(), notes);
            req.getSession().setAttribute("success", "Application status updated to " + newStatus);
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Failed to update status: " + e.getMessage());
        }

        if (redirectJobId != null && !redirectJobId.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/employer/applicants?jobId=" + redirectJobId);
        } else {
            resp.sendRedirect(req.getContextPath() + "/employer/jobs");
        }
    }
}
