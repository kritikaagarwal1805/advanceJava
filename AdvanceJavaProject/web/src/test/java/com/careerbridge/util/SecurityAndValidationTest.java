package com.careerbridge.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityAndValidationTest {

    @Test
    @DisplayName("Password hashing with PBKDF2 generates salted verifyable hashes")
    public void testPasswordHashing() {
        String rawPassword = "SecurePassword@123";
        String hash = PasswordUtil.hashPassword(rawPassword);

        assertNotNull(hash);
        assertTrue(hash.contains(":"));
        assertTrue(PasswordUtil.verifyPassword(rawPassword, hash));
        assertFalse(PasswordUtil.verifyPassword("WrongPassword", hash));
    }

    @Test
    @DisplayName("ValidationUtil checks valid and invalid emails")
    public void testEmailValidation() {
        assertTrue(ValidationUtil.isValidEmail("student@rtu.ac.in"));
        assertTrue(ValidationUtil.isValidEmail("recruiter@company.com"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    @DisplayName("HTML Escaping prevents Cross-Site Scripting (XSS)")
    public void testHtmlEscaping() {
        String xss = "<script>alert('pwned')</script>";
        String safe = ValidationUtil.escapeHtml(xss);
        assertEquals("&lt;script&gt;alert(&#x27;pwned&#x27;)&lt;/script&gt;", safe);
    }
}
