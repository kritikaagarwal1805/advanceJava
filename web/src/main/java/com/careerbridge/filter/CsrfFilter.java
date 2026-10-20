package com.careerbridge.filter;

import com.careerbridge.util.CsrfUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Filter that provides Cross-Site Request Forgery (CSRF) protection.
 * - Sets the csrfToken request attribute on every request for JSP views.
 * - Enforces CSRF token validation on POST requests (skipping /login and /register).
 * - Responds with HTTP 403 Forbidden when CSRF verification fails.
 */
@WebFilter(filterName = "CsrfFilter", urlPatterns = "/*")
public class CsrfFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Ensure session exists and set csrfToken attribute for JSP rendering
        HttpSession session = httpRequest.getSession(true);
        String csrfToken = CsrfUtil.getToken(session);
        httpRequest.setAttribute("csrfToken", csrfToken);

        // Check if method is POST
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            String contextPath = httpRequest.getContextPath();
            String requestURI = httpRequest.getRequestURI();
            String path = requestURI.substring(contextPath.length());

            // Skip CSRF validation for login and register
            if (path.equals("/login") || path.equals("/register")) {
                chain.doFilter(request, response);
                return;
            }

            // Validate token
            if (!isValidCsrf(httpRequest, session)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing CSRF token.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isValidCsrf(HttpServletRequest request, HttpSession session) {
        if (CsrfUtil.isValid(request)) {
            return true;
        }

        if (session == null) {
            return false;
        }

        String sessionToken = (String) session.getAttribute("csrfToken");
        if (sessionToken == null) {
            return false;
        }

        // Check request header (e.g. X-CSRF-TOKEN)
        String headerToken = request.getHeader("X-CSRF-TOKEN");
        if (headerToken != null && sessionToken.equals(headerToken)) {
            return true;
        }

        // Check query string for multipart POST forms
        String queryString = request.getQueryString();
        if (queryString != null) {
            for (String param : queryString.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2 && "_csrf".equals(pair[0])) {
                    try {
                        String val = URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                        if (sessionToken.equals(val)) {
                            return true;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        // For multipart requests, attempt to read part if available
        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")) {
            try {
                var part = request.getPart("_csrf");
                if (part != null) {
                    String partValue = new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
                    if (sessionToken.equals(partValue)) {
                        return true;
                    }
                }
            } catch (Exception ignored) {
                // If container does not parse multipart in filter, handled by query param or servlet
            }
        }

        return false;
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
