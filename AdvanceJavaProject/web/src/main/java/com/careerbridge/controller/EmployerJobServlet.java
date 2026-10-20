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
import java.util.List;

@WebServlet("/employer/jobs")
public class EmployerJobServlet extends HttpServlet {
    private final JobService jobService = new JobService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<Job> jobs = jobService.getJobsByOwner(user.getUserId());
        req.setAttribute("jobs", jobs);
        req.getRequestDispatcher("/employer/jobs.jsp").forward(req, resp);
    }
}
