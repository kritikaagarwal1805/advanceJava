package com.careerbridge.listener;

import com.careerbridge.util.DatabaseUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application lifecycle listener that initializes resources upon startup
 * and performs cleanup on shutdown.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("CareerBridge application is initializing...");
        ServletContext context = sce.getServletContext();

        try {
            DatabaseUtil.initialize();
            LOGGER.info("DatabaseUtil initialized successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database initialization warning: " + e.getMessage(), e);
        }

        context.setAttribute("appName", "CareerBridge");
        context.setAttribute("appVersion", "1.0.0");
        LOGGER.info("CareerBridge application started successfully.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("CareerBridge application is stopping...");

        // Deregister JDBC drivers to prevent memory leaks on undeploy
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOGGER.info("Deregistered JDBC driver: " + driver);
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error deregistering driver: " + driver, e);
            }
        }

        LOGGER.info("CareerBridge application shutdown complete.");
    }
}
