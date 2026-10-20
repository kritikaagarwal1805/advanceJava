package com.careerbridge.dao;

import com.careerbridge.model.Category;
import com.careerbridge.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for job Categories and Job-Category associations.
 */
public class CategoryDAO {

    /**
     * Retrieve all job categories ordered by name.
     *
     * @return List of Category objects
     * @throws SQLException on database error
     */
    public List<Category> findAll() throws SQLException {
        String sql = "SELECT * FROM categories ORDER BY name ASC";

        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapRow(rs));
            }
        }
        return categories;
    }

    /**
     * Find a category by primary key ID.
     *
     * @param categoryId category ID
     * @return Optional containing Category if found
     * @throws SQLException on database error
     */
    public Optional<Category> findById(long categoryId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Find a category by name (case-insensitive).
     *
     * @param name category name
     * @return Optional containing Category if found
     * @throws SQLException on database error
     */
    public Optional<Category> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM categories WHERE LOWER(name) = LOWER(?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Create a new category.
     *
     * @param category Category entity to persist
     * @return generated category_id
     * @throws SQLException on database error
     */
    public long create(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    category.setCategoryId(id);
                    return id;
                }
            }
        }
        throw new SQLException("Failed to create category, no ID obtained.");
    }

    /**
     * Retrieve all categories mapped to a given job.
     *
     * @param jobId job ID
     * @return List of Category objects
     * @throws SQLException on database error
     */
    public List<Category> findByJobId(long jobId) throws SQLException {
        String sql = "SELECT c.* FROM categories c " +
                     "JOIN job_categories jc ON c.category_id = jc.category_id " +
                     "WHERE jc.job_id = ? " +
                     "ORDER BY c.name ASC";

        List<Category> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /**
     * Associate a category with a job using INSERT IGNORE.
     *
     * @param jobId job ID
     * @param categoryId category ID
     * @return true if associated
     * @throws SQLException on database error
     */
    public boolean addCategoryToJob(long jobId, long categoryId) throws SQLException {
        String sql = "INSERT IGNORE INTO job_categories (job_id, category_id) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            ps.setLong(2, categoryId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Remove an association between a category and a job.
     *
     * @param jobId job ID
     * @param categoryId category ID
     * @return true if removed
     * @throws SQLException on database error
     */
    public boolean removeCategoryFromJob(long jobId, long categoryId) throws SQLException {
        String sql = "DELETE FROM job_categories WHERE job_id = ? AND category_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, jobId);
            ps.setLong(2, categoryId);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Map a ResultSet row to a Category object.
     */
    private Category mapRow(ResultSet rs) throws SQLException {
        Category cat = new Category();
        cat.setCategoryId(rs.getLong("category_id"));
        cat.setName(rs.getString("name"));
        cat.setDescription(rs.getString("description"));
        return cat;
    }
}
