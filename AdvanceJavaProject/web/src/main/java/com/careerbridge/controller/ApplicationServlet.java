package com.careerbridge.controller;

import com.careerbridge.model.User;
import com.careerbridge.service.ApplicationService;
import com.careerbridge.service.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/candidate/apply")
public class ApplicationServlet extends HttpServlet {
    private final ApplicationService applicationService = new ApplicationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.JOB_SEEKER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String jobIdStr = req.getParameter("jobId");
        String coverLetter = req.getParameter("coverLetter");
        String resumeIdStr = req.getParameter("resumeId");

        try {
            long jobId = Long.parseLong(jobIdStr);
            Long resumeId = (resumeIdStr != null && !resumeIdStr.isBlank()) ? Long.parseLong(resumeIdStr) : null;

            applicationService.apply(jobId, user.getUserId(), resumeId, coverLetter);
            req.getSession().setAttribute("success", "Application submitted successfully!");
            resp.sendRedirect(req.getContextPath() + "/candidate/applications");
        } catch (ServiceException e) {
            req.getSession().setAttribute("error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/job-details?id=" + jobIdStr);
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Application failed: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/job-details?id=" + jobIdStr);
        }
    }
}
