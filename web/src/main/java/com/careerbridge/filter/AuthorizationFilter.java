package com.careerbridge.filter;

import com.careerbridge.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter that enforces role-based access control (RBAC).
 * Enforces:
 *   - /candidate/* requires JOB_SEEKER role
 *   - /employer/*  requires EMPLOYER role
 *   - /admin/*     requires ADMIN role
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = "/*")
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();
        String requestURI = httpRequest.getRequestURI();
        String path = requestURI.substring(contextPath.length());

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (path.startsWith("/candidate/")) {
            if (user == null) {
                httpResponse.sendRedirect(contextPath + "/login");
                return;
            }
            if (user.getRole() != User.Role.JOB_SEEKER) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Candidate role required.");
                return;
            }
        } else if (path.startsWith("/employer/")) {
            if (user == null) {
                httpResponse.sendRedirect(contextPath + "/login");
                return;
            }
            if (user.getRole() != User.Role.EMPLOYER) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Employer role required.");
                return;
            }
        } else if (path.startsWith("/admin/")) {
            if (user == null) {
                httpResponse.sendRedirect(contextPath + "/login");
                return;
            }
            if (user.getRole() != User.Role.ADMIN) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin role required.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
