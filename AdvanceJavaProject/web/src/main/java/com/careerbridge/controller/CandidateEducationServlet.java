package com.careerbridge.controller;

import com.careerbridge.model.Education;
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
import java.time.LocalDate;
import java.util.List;

@WebServlet("/candidate/education")
public class CandidateEducationServlet extends HttpServlet {
    private final CandidateProfileService profileService = new CandidateProfileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Education> educationList = profileService.getEducation(user.getUserId());
        req.setAttribute("educationList", educationList);
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
        req.getRequestDispatcher("/candidate/education.jsp").forward(req, resp);
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
            if ("delete".equalsIgnoreCase(action)) {
                long educationId = Long.parseLong(req.getParameter("educationId"));
                profileService.deleteEducation(educationId, user.getUserId());
                req.getSession().setAttribute("success", "Education record deleted.");
            } else {
                Education edu = new Education();
                edu.setCandidateUserId(user.getUserId());
                edu.setInstitution(req.getParameter("institution"));
                edu.setQualification(req.getParameter("qualification"));
                edu.setFieldOfStudy(req.getParameter("fieldOfStudy"));
                edu.setGrade(req.getParameter("grade"));

                String start = req.getParameter("startDate");
                String end = req.getParameter("endDate");
                if (start != null && !start.isBlank()) edu.setStartDate(LocalDate.parse(start));
                if (end != null && !end.isBlank()) edu.setEndDate(LocalDate.parse(end));

                String idStr = req.getParameter("educationId");
                if (idStr != null && !idStr.isBlank()) {
                    edu.setEducationId(Long.parseLong(idStr));
                    profileService.updateEducation(edu, user.getUserId());
                    req.getSession().setAttribute("success", "Education record updated.");
                } else {
                    profileService.addEducation(edu);
                    req.getSession().setAttribute("success", "Education record added.");
                }
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Action failed: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/candidate/education");
    }
}
