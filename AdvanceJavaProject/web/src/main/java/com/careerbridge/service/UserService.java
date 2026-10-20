package com.careerbridge.service;

import com.careerbridge.dao.UserDAO;
import com.careerbridge.model.User;
import java.sql.SQLException;
import java.util.*;

/**
 * User management service (admin operations).
 */
public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public Optional<User> getUserById(long userId) {
        try { return userDAO.findById(userId); }
        catch (SQLException e) { throw new ServiceException("Failed to load user.", e); }
    }

    public List<User> getAllUsers(String roleFilter, String statusFilter, int page, int pageSize) {
        try { return userDAO.findAll(roleFilter, statusFilter, page, pageSize); }
        catch (SQLException e) { throw new ServiceException("Failed to load users.", e); }
    }

    public int getUserCount(String roleFilter, String statusFilter) {
        try { return userDAO.count(roleFilter, statusFilter); }
        catch (SQLException e) { throw new ServiceException("Failed to count users.", e); }
    }

    public boolean updateUserStatus(long userId, User.Status status) {
        try { return userDAO.updateStatus(userId, status); }
        catch (SQLException e) { throw new ServiceException("Failed to update user status.", e); }
    }

    public Map<String, Integer> getUserCountsByRole() {
        try { return userDAO.getCountsByRole(); }
        catch (SQLException e) { throw new ServiceException("Failed to get user stats.", e); }
    }
}
