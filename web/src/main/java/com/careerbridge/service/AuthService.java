package com.careerbridge.service;

import com.careerbridge.dao.UserDAO;
import com.careerbridge.model.User;
import com.careerbridge.util.PasswordUtil;
import com.careerbridge.util.ValidationUtil;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Authentication service: registration, login, password management.
 */
public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User register(String fullName, String email, String password, String role) {
        if (!ValidationUtil.isValidName(fullName))
            throw new ServiceException("Full name must be 2-100 characters.");
        if (!ValidationUtil.isValidEmail(email))
            throw new ServiceException("Invalid email address.");
        if (!ValidationUtil.isValidPassword(password))
            throw new ServiceException("Password must be at least 8 characters.");

        User.Role userRole;
        try {
            userRole = User.Role.valueOf(role);
        } catch (Exception e) {
            throw new ServiceException("Invalid role selected.");
        }
        if (userRole == User.Role.ADMIN) {
            throw new ServiceException("Cannot register as admin.");
        }

        try {
            if (userDAO.emailExists(email.trim().toLowerCase())) {
                throw new ServiceException("An account with this email already exists.");
            }
            String hash = PasswordUtil.hashPassword(password);
            User user = new User(fullName.trim(), email.trim().toLowerCase(), hash, userRole);
            userDAO.create(user);
            return user;
        } catch (SQLException e) {
            throw new ServiceException("Registration failed. Please try again.", e);
        }
    }

    public Optional<User> login(String email, String password) {
        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
            return Optional.empty();
        }
        try {
            Optional<User> opt = userDAO.findByEmail(email.trim().toLowerCase());
            if (opt.isPresent()) {
                User user = opt.get();
                if (user.getStatus() != User.Status.ACTIVE) return Optional.empty();
                if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new ServiceException("Login failed.", e);
        }
    }

    public boolean changePassword(long userId, String oldPassword, String newPassword) {
        if (!ValidationUtil.isValidPassword(newPassword))
            throw new ServiceException("New password must be at least 8 characters.");
        try {
            Optional<User> opt = userDAO.findById(userId);
            if (opt.isEmpty()) throw new ServiceException("User not found.");
            User user = opt.get();
            if (!PasswordUtil.verifyPassword(oldPassword, user.getPasswordHash())) {
                throw new ServiceException("Current password is incorrect.");
            }
            return userDAO.updatePassword(userId, PasswordUtil.hashPassword(newPassword));
        } catch (SQLException e) {
            throw new ServiceException("Password change failed.", e);
        }
    }
}
