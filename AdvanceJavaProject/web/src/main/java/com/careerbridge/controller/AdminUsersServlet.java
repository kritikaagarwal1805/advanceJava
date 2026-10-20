package com.careerbridge.controller;

import com.careerbridge.model.User;
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

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = req.getParameter("role");
        String status = req.getParameter("status");
        int page = 1;
        try {
            String p = req.getParameter("page");
            if (p != null) page = Math.max(1, Integer.parseInt(p));
        } catch (NumberFormatException ignored) {}

        int pageSize = 10;
        List<User> users = userService.getAllUsers(role, status, page, pageSize);
        int totalUsers = userService.getUserCount(role, status);
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

        req.setAttribute("users", users);
        req.setAttribute("roleFilter", role);
        req.setAttribute("statusFilter", status);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));

        req.getRequestDispatcher("/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String userIdStr = req.getParameter("userId");
        String newStatusStr = req.getParameter("newStatus");

        try {
            long userId = Long.parseLong(userIdStr);
            User.Status newStatus = User.Status.valueOf(newStatusStr);
            userService.updateUserStatus(userId, newStatus);
            req.getSession().setAttribute("success", "User status updated to " + newStatus);
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Failed to update user: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
