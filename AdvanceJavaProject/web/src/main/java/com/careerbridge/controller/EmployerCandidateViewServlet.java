package com.careerbridge.controller;

import com.careerbridge.model.*;
import com.careerbridge.service.ApplicationService;
import com.careerbridge.service.CandidateProfileService;
import com.careerbridge.service.ResumeService;
import com.careerbridge.service.UserService;
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

@WebServlet("/employer/candidate-view")
public class EmployerCandidateViewServlet extends HttpServlet {
    private final ApplicationService applicationService = new ApplicationService();
    private final CandidateProfileService profileService = new CandidateProfileService();
    private final UserService userService = new UserService();
    private final ResumeService resumeService = new ResumeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String appIdStr = req.getParameter("applicationId");
        if (appIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/employer/jobs");
            return;
        }

        try {
            long appId = Long.parseLong(appIdStr);
            if (!applicationService.isApplicationAccessibleByEmployer(appId, user.getUserId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied to candidate profile");
                return;
            }

            Optional<JobApplication> appOpt = applicationService.getApplicationById(appId);
            if (appOpt.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Application not found");
                return;
            }

            JobApplication application = appOpt.get();
            req.setAttribute("application", application);

            long candidateId = application.getCandidateUserId();
            userService.getUserById(candidateId).ifPresent(u -> req.setAttribute("candidateUser", u));
            profileService.getProfile(candidateId).ifPresent(p -> req.setAttribute("profile", p));

            List<Education> educationList = profileService.getEducation(candidateId);
            req.setAttribute("educationList", educationList);

            List<CandidateSkill> skills = profileService.getCandidateSkills(candidateId);
            req.setAttribute("candidateSkills", skills);

            if (application.getResumeId() != null) {
                resumeService.getResume(application.getResumeId()).ifPresent(r -> req.setAttribute("resume", r));
            }

            req.setAttribute("allowedTransitions", JobApplication.getAllowedTransitions(application.getStatus()));
            req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
            req.getRequestDispatcher("/employer/candidate-view.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/employer/jobs");
        }
    }
}
