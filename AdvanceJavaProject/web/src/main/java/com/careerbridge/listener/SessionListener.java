package com.careerbridge.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Session listener that tracks the count of active HTTP sessions.
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    private static final Logger LOGGER = Logger.getLogger(SessionListener.class.getName());
    private static final AtomicInteger activeSessionCount = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int current = activeSessionCount.incrementAndGet();
        LOGGER.info("Session created: " + se.getSession().getId() + " | Active sessions: " + current);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int current = activeSessionCount.decrementAndGet();
        if (current < 0) {
            activeSessionCount.set(0);
            current = 0;
        }
        LOGGER.info("Session destroyed: " + se.getSession().getId() + " | Active sessions: " + current);
    }

    /**
     * Gets the number of currently active sessions.
     *
     * @return current active session count
     */
    public static int getActiveSessionCount() {
        return activeSessionCount.get();
    }
}
