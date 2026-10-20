package com.careerbridge.controller;

import com.careerbridge.model.CandidateProfile;
import com.careerbridge.model.User;
import com.careerbridge.service.CandidateProfileService;
import com.careerbridge.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/candidate/profile")
public class CandidateProfileServlet extends HttpServlet {
    private final CandidateProfileService profileService = new CandidateProfileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Optional<CandidateProfile> profile = profileService.getProfile(user.getUserId());
        profile.ifPresent(p -> req.setAttribute("profile", p));
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
        req.getRequestDispatcher("/candidate/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        CandidateProfile profile = new CandidateProfile();
        profile.setUserId(user.getUserId());
        profile.setHeadline(req.getParameter("headline"));
        profile.setSummary(req.getParameter("summary"));
        profile.setPhone(req.getParameter("phone"));
        profile.setLocation(req.getParameter("location"));
        profile.setPortfolioUrl(req.getParameter("portfolioUrl"));
        profile.setLinkedinUrl(req.getParameter("linkedinUrl"));

        try {
            profileService.saveProfile(profile);
            req.getSession().setAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Failed to update profile: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/candidate/profile");
    }
}
