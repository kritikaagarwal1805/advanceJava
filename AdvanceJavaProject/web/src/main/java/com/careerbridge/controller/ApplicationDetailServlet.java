package com.careerbridge.controller;

import com.careerbridge.model.ApplicationStatusHistory;
import com.careerbridge.model.JobApplication;
import com.careerbridge.model.User;
import com.careerbridge.service.ApplicationService;
import com.careerbridge.service.ResumeService;
import com.careerbridge.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/candidate/application-detail")
public class ApplicationDetailServlet extends HttpServlet {
    private final ApplicationService applicationService = new ApplicationService();
    private final ResumeService resumeService = new ResumeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.JOB_SEEKER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/candidate/applications");
            return;
        }

        try {
            long appId = Long.parseLong(idStr);
            Optional<JobApplication> appOpt = applicationService.getApplicationById(appId);
            if (appOpt.isEmpty() || appOpt.get().getCandidateUserId() != user.getUserId()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Application not found or unauthorized");
                return;
            }

            JobApplication application = appOpt.get();
            req.setAttribute("application", application);
            if (application.getResumeId() != null) {
                resumeService.getResume(application.getResumeId()).ifPresent(r -> req.setAttribute("resume", r));
            }

            List<ApplicationStatusHistory> history = applicationService.getStatusHistory(appId);
            req.setAttribute("statusHistory", history);

            boolean canWithdraw = JobApplication.getAllowedTransitions(application.getStatus())
                    .contains(JobApplication.Status.WITHDRAWN);
            req.setAttribute("canWithdraw", canWithdraw);
            req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));

            req.getRequestDispatcher("/candidate/application-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/candidate/applications");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.JOB_SEEKER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("applicationId");
        String action = req.getParameter("action");

        if ("withdraw".equalsIgnoreCase(action) && idStr != null) {
            try {
                long appId = Long.parseLong(idStr);
                applicationService.withdrawApplication(appId, user.getUserId());
                req.getSession().setAttribute("success", "Application successfully withdrawn.");
            } catch (Exception e) {
                req.getSession().setAttribute("error", "Withdrawal failed: " + e.getMessage());
            }
        }
        resp.sendRedirect(req.getContextPath() + "/candidate/application-detail?id=" + idStr);
    }
}
