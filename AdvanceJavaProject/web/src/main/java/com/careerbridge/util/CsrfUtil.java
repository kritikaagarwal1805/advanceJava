package com.careerbridge.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF token generation and validation.
 * Generates a per-session token stored in the session.
 */
public class CsrfUtil {
    private static final String CSRF_TOKEN_ATTR = "csrfToken";
    private static final String CSRF_PARAM_NAME = "_csrf";
    private static final int TOKEN_LENGTH = 32;

    private CsrfUtil() {}

    /**
     * Get or generate a CSRF token for the session.
     */
    public static String getToken(HttpSession session) {
        String token = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        if (token == null) {
            token = generateToken();
            session.setAttribute(CSRF_TOKEN_ATTR, token);
        }
        return token;
    }

    /**
     * Validate the CSRF token from the request.
     */
    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTR);
        String requestToken = request.getParameter(CSRF_PARAM_NAME);
        if (sessionToken == null || requestToken == null) return false;
        return sessionToken.equals(requestToken);
    }

    public static String getParameterName() {
        return CSRF_PARAM_NAME;
    }

    private static String generateToken() {
        byte[] bytes = new byte[TOKEN_LENGTH];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
