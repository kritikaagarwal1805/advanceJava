package com.careerbridge.util;

import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 * Manages JDBC database connections.
 * Loads configuration from db.properties file.
 * Demonstrates JDBC connection management for RTU syllabus.
 */
public class DatabaseUtil {
    private static String url;
    private static String username;
    private static String password;
    private static String driver;
    private static boolean initialized = false;

    private DatabaseUtil() {} // Prevent instantiation

    /**
     * Initialize database configuration from properties file.
     * Looks for db.properties in classpath first, then in project root.
     */
    public static synchronized void initialize() {
        if (initialized) return;
        Properties props = new Properties();
        
        // Try classpath first
        try (InputStream is = DatabaseUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                // Try file system
                File file = new File(System.getProperty("user.dir"), "db.properties");
                if (file.exists()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        props.load(fis);
                    }
                } else {
                    throw new RuntimeException("db.properties not found. Copy db.properties.example to db.properties and configure.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }

        url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/careerbridge");
        username = props.getProperty("db.username", "root");
        password = props.getProperty("db.password", "");
        driver = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
        initialized = true;
    }

    /**
     * Get a database connection.
     * Demonstrates JDBC Connection management.
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) initialize();
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Close resources safely. Demonstrates proper JDBC cleanup.
     */
    public static void close(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    System.err.println("Error closing resource: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Test database connectivity.
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    // Allow re-initialization (for testing)
    public static synchronized void reset() {
        initialized = false;
    }
}
