package com.careerbridge.util;

import java.util.regex.Pattern;

/**
 * Server-side input validation utilities.
 */
public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{7,15}$"
    );
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^https?://[\\w.-]+(?:/[\\w._~:/?#\\[\\]@!$&'()*+,;=-]*)?$"
    );
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_EMAIL_LENGTH = 255;
    private static final int MAX_TEXT_LENGTH = 5000;
    private static final int MIN_PASSWORD_LENGTH = 8;

    private ValidationUtil() {}

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String sanitize(String input) {
        if (input == null) return null;
        return input.trim();
    }

    public static boolean isValidEmail(String email) {
        return !isNullOrEmpty(email) && email.length() <= MAX_EMAIL_LENGTH && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !isNullOrEmpty(password) && password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static boolean isValidName(String name) {
        return !isNullOrEmpty(name) && name.trim().length() >= 2 && name.length() <= MAX_NAME_LENGTH;
    }

    public static boolean isValidPhone(String phone) {
        return isNullOrEmpty(phone) || PHONE_PATTERN.matcher(phone.replaceAll("[\\s-]", "")).matches();
    }

    public static boolean isValidUrl(String url) {
        return isNullOrEmpty(url) || (url.length() <= 500 && URL_PATTERN.matcher(url).matches());
    }

    public static boolean isValidTextLength(String text, int maxLength) {
        return text == null || text.length() <= maxLength;
    }

    public static boolean isValidId(String idStr) {
        if (isNullOrEmpty(idStr)) return false;
        try {
            return Long.parseLong(idStr) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static long parseId(String idStr) {
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Encode string for safe HTML output to prevent XSS.
     */
    public static String escapeHtml(String input) {
        if (input == null) return null;
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;");
    }
}
