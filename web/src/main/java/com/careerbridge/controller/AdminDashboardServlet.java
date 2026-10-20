package com.careerbridge.controller;

import com.careerbridge.model.User;
import com.careerbridge.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Map<String, Object> stats = adminService.getDashboardStats();
        req.setAttribute("stats", stats);
        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }
}
