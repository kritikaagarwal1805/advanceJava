package com.careerbridge.controller;

import com.careerbridge.model.CandidateSkill;
import com.careerbridge.model.Skill;
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
import java.util.List;

@WebServlet("/candidate/skills")
public class CandidateSkillsServlet extends HttpServlet {
    private final CandidateProfileService profileService = new CandidateProfileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<CandidateSkill> candidateSkills = profileService.getCandidateSkills(user.getUserId());
        List<Skill> allSkills = profileService.getAllSkills();
        req.setAttribute("candidateSkills", candidateSkills);
        req.setAttribute("allSkills", allSkills);
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
        req.getRequestDispatcher("/candidate/skills.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        try {
            if ("remove".equalsIgnoreCase(action)) {
                long skillId = Long.parseLong(req.getParameter("skillId"));
                profileService.removeSkill(user.getUserId(), skillId);
                req.getSession().setAttribute("success", "Skill removed.");
            } else {
                long skillId = Long.parseLong(req.getParameter("skillId"));
                String level = req.getParameter("proficiencyLevel");
                profileService.addSkill(user.getUserId(), skillId, level);
                req.getSession().setAttribute("success", "Skill added.");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Skill operation failed: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/candidate/skills");
    }
}
