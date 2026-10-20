package com.careerbridge.controller;

import com.careerbridge.model.User;
import com.careerbridge.service.AuthService;
import com.careerbridge.service.ServiceException;
import com.careerbridge.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // If already logged in, redirect to dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            resp.sendRedirect(req.getContextPath() + getDashboardPath(user.getRole()));
            return;
        }
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        var optUser = authService.login(email, password);
        if (optUser.isPresent()) {
            User user = optUser.get();
            // Session fixation prevention: invalidate old session, create new
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) oldSession.invalidate();
            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("user", user);
            newSession.setAttribute("userId", user.getUserId());
            newSession.setAttribute("userRole", user.getRole().name());
            newSession.setMaxInactiveInterval(30 * 60);
            resp.sendRedirect(req.getContextPath() + getDashboardPath(user.getRole()));
        } else {
            req.setAttribute("error", "Invalid email or password.");
            req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private String getDashboardPath(User.Role role) {
        return switch (role) {
            case EMPLOYER -> "/employer/dashboard";
            case ADMIN -> "/admin/dashboard";
            default -> "/candidate/dashboard";
        };
    }
}
