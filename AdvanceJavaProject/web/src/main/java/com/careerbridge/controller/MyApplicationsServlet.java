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
import java.util.List;

@WebServlet("/candidate/applications")
public class MyApplicationsServlet extends HttpServlet {
    private final ApplicationService applicationService = new ApplicationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.JOB_SEEKER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<JobApplication> applications = applicationService.getApplicationsByCandidate(user.getUserId());
        req.setAttribute("applications", applications);
        req.getRequestDispatcher("/candidate/applications.jsp").forward(req, resp);
    }
}
