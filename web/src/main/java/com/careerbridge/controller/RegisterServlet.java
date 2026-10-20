package com.careerbridge.controller;

import com.careerbridge.service.AuthService;
import com.careerbridge.service.ServiceException;
import com.careerbridge.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String role = req.getParameter("role");

        if (password != null && !password.equals(confirmPassword)) {
            req.setAttribute("error", "Passwords do not match.");
            preserveFields(req, fullName, email, role);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        try {
            authService.register(fullName, email, password, role);
            req.getSession().setAttribute("success", "Registration successful! Please login.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (ServiceException e) {
            req.setAttribute("error", e.getMessage());
            preserveFields(req, fullName, email, role);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }

    private void preserveFields(HttpServletRequest req, String name, String email, String role) {
        req.setAttribute("fullName", name);
        req.setAttribute("email", email);
        req.setAttribute("selectedRole", role);
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
    }
}
