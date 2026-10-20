package com.careerbridge.controller;

import com.careerbridge.model.CandidateProfile;
import com.careerbridge.model.JobApplication;
import com.careerbridge.model.ResumeMetadata;
import com.careerbridge.model.User;
import com.careerbridge.service.ApplicationService;
import com.careerbridge.service.CandidateProfileService;
import com.careerbridge.service.ResumeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/candidate/dashboard")
public class CandidateDashboardServlet extends HttpServlet {
    private final CandidateProfileService profileService = new CandidateProfileService();
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

        long userId = user.getUserId();
        Optional<CandidateProfile> profile = profileService.getProfile(userId);
        profile.ifPresent(p -> req.setAttribute("profile", p));

        List<JobApplication> applications = applicationService.getApplicationsByCandidate(userId);
        req.setAttribute("applications", applications);
        req.setAttribute("totalApplications", applications.size());

        long underReview = applications.stream().filter(a -> a.getStatus() == JobApplication.Status.UNDER_REVIEW).count();
        long shortlisted = applications.stream().filter(a -> a.getStatus() == JobApplication.Status.SHORTLISTED || a.getStatus() == JobApplication.Status.INTERVIEW).count();
        long selected = applications.stream().filter(a -> a.getStatus() == JobApplication.Status.SELECTED).count();

        req.setAttribute("underReviewCount", underReview);
        req.setAttribute("shortlistedCount", shortlisted);
        req.setAttribute("selectedCount", selected);

        List<ResumeMetadata> resumes = resumeService.getResumesByCandidate(userId);
        req.setAttribute("resumes", resumes);
        req.setAttribute("resumeCount", resumes.size());

        req.getRequestDispatcher("/candidate/dashboard.jsp").forward(req, resp);
    }
}
