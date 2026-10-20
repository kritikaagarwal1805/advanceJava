package com.careerbridge.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CareerBridge Socket Server Demo.
 * Multi-threaded TCP server listening on port 5000 providing job query services.
 *
 * RTU Syllabus: Socket Programming, ServerSocket, Client-Server Communication,
 * Multi-threading in Networking, Stream I/O.
 */
public class JobServer {

    public static final int DEFAULT_PORT = 5000;
    private static final int READ_TIMEOUT_MS = 60000; // 60 seconds read timeout

    // In-memory job repository (simulates DB)
    private static final Map<Long, JobRecord> JOB_STORE = new ConcurrentHashMap<>();

    private final int port;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    // Detailed internal record
    public static class JobRecord {
        private final long id;
        private final String title;
        private final String company;
        private final String location;
        private final String type;
        private final String status;
        private final String salary;
        private final String description;

        public JobRecord(long id, String title, String company, String location,
                         String type, String status, String salary, String description) {
            this.id = id;
            this.title = title;
            this.company = company;
            this.location = location;
            this.type = type;
            this.status = status;
            this.salary = salary;
            this.description = description;
        }

        public long getId() { return id; }
        public String getTitle() { return title; }
        public String getCompany() { return company; }
        public String getLocation() { return location; }
        public String getType() { return type; }
        public String getStatus() { return status; }
        public String getSalary() { return salary; }
        public String getDescription() { return description; }

        public String toSummaryString() {
            return String.format("[%d] %s | Company: %s | Loc: %s | Type: %s | Status: %s",
                    id, title, company, location, type, status);
        }

        public String toDetailString() {
            return String.format("ID=%d; Title=%s; Company=%s; Location=%s; Type=%s; Status=%s; Salary=%s; Desc=%s",
                    id, title, company, location, type, status, salary, description);
        }
    }

    static {
        // Pre-populate with realistic in-memory job data
        seedSampleJobs();
    }

    private static void seedSampleJobs() {
        JOB_STORE.put(101L, new JobRecord(101L, "Senior Java Developer", "Google India", "Bengaluru",
                "Full-time", "Published", "INR 28-35 LPA", "Build scalable microservices with Spring Boot & Kafka."));
        JOB_STORE.put(102L, new JobRecord(102L, "Full Stack Engineer", "Microsoft", "Hyderabad",
                "Full-time", "Published", "INR 22-30 LPA", "React frontend and Azure cloud backend development."));
        JOB_STORE.put(103L, new JobRecord(103L, "Cloud DevOps Architect", "Amazon AWS", "Remote",
                "Full-time", "Published", "INR 30-40 LPA", "CI/CD automation, Kubernetes clusters, and security."));
        JOB_STORE.put(104L, new JobRecord(104L, "Backend Intern (Jakarta EE)", "Infosys", "Pune",
                "Internship", "Published", "INR 25,000/mo", "Hands-on Jakarta Servlets, JSP, and MySQL integration."));
        JOB_STORE.put(105L, new JobRecord(105L, "UI/UX Consultant", "TCS", "Jaipur",
                "Contract", "Draft", "INR 12-15 LPA", "User research, wireframing, and interactive prototype testing."));
        JOB_STORE.put(106L, new JobRecord(106L, "Database Administrator", "Oracle", "Bengaluru",
                "Full-time", "Closed", "INR 18-24 LPA", "MySQL and Oracle DB tuning, backup, and replication."));
    }

    public JobServer(int port) {
        this.port = port;
    }

    /**
     * Starts the TCP server socket loop.
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running.set(true);
        threadPool = Executors.newCachedThreadPool();

        log("CareerBridge TCP JobServer started on port " + port);
        log("Ready to accept incoming client connections. Read timeout: " + (READ_TIMEOUT_MS / 1000) + "s");

        try {
            while (running.get()) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(READ_TIMEOUT_MS);
                log("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            if (running.get()) {
                log("Server socket error: " + e.getMessage());
            }
        } finally {
            stop();
        }
    }

    /**
     * Gracefully stops the server and closes connections.
     */
    public void stop() {
        if (!running.getAndSet(false)) return;
        log("Shutting down JobServer...");
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {}
        if (threadPool != null) {
            threadPool.shutdownNow();
        }
        log("JobServer shutdown complete.");
    }

    /**
     * Handles an individual client connection in an independent thread.
     */
    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String clientAddr = socket.getRemoteSocketAddress().toString();
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // Initial greeting banner
                writer.println("220 Welcome to CareerBridge TCP JobServer [Ready]");
                writer.println("Commands: SEARCH_JOBS <keyword> | GET_JOB <id> | GET_STATS | QUIT");

                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }

                    log("[" + clientAddr + "] Received command: " + line);

                    if ("QUIT".equalsIgnoreCase(line)) {
                        writer.println("200 BYE: Connection closing.");
                        break;
                    } else if (line.toUpperCase().startsWith("SEARCH_JOBS")) {
                        handleSearch(line, writer);
                    } else if (line.toUpperCase().startsWith("GET_JOB")) {
                        handleGetJob(line, writer);
                    } else if ("GET_STATS".equalsIgnoreCase(line)) {
                        handleGetStats(writer);
                    } else {
                        writer.println("400 ERROR: Unknown command. Supported: SEARCH_JOBS <kw>, GET_JOB <id>, GET_STATS, QUIT");
                    }
                }
            } catch (SocketTimeoutException te) {
                log("[" + clientAddr + "] Connection timed out after " + (READ_TIMEOUT_MS / 1000) + "s of inactivity.");
            } catch (IOException e) {
                log("[" + clientAddr + "] Communication error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                log("[" + clientAddr + "] Client disconnected.");
            }
        }

        private void handleSearch(String line, PrintWriter writer) {
            String[] parts = line.split("\\s+", 2);
            String keyword = (parts.length > 1) ? parts[1].trim().toLowerCase() : "";

            List<JobRecord> matches = new ArrayList<>();
            for (JobRecord job : JOB_STORE.values()) {
                if (keyword.isEmpty()
                        || job.getTitle().toLowerCase().contains(keyword)
                        || job.getCompany().toLowerCase().contains(keyword)
                        || job.getLocation().toLowerCase().contains(keyword)
                        || job.getType().toLowerCase().contains(keyword)) {
                    matches.add(job);
                }
            }

            writer.println("200 FOUND " + matches.size() + " MATCHES:");
            for (JobRecord job : matches) {
                writer.println(job.toSummaryString());
            }
            writer.println("END");
        }

        private void handleGetJob(String line, PrintWriter writer) {
            String[] parts = line.split("\\s+");
            if (parts.length < 2) {
                writer.println("400 ERROR: Syntax is GET_JOB <id>");
                return;
            }

            try {
                long id = Long.parseLong(parts[1]);
                JobRecord record = JOB_STORE.get(id);
                if (record != null) {
                    writer.println("200 OK: " + record.toDetailString());
                } else {
                    writer.println("404 ERROR: Job with ID " + id + " not found.");
                }
            } catch (NumberFormatException e) {
                writer.println("400 ERROR: Invalid Job ID format. Must be numeric.");
            }
        }

        private void handleGetStats(PrintWriter writer) {
            int total = JOB_STORE.size();
            long published = JOB_STORE.values().stream().filter(j -> "Published".equalsIgnoreCase(j.getStatus())).count();
            long draft = JOB_STORE.values().stream().filter(j -> "Draft".equalsIgnoreCase(j.getStatus())).count();
            long closed = JOB_STORE.values().stream().filter(j -> "Closed".equalsIgnoreCase(j.getStatus())).count();

            writer.println(String.format("200 STATS: Total=%d; Published=%d; Draft=%d; Closed=%d",
                    total, published, draft, closed));
        }
    }

    private static void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[" + timestamp + "] [JobServer] " + message);
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port specified. Using default: " + DEFAULT_PORT);
            }
        }

        JobServer server = new JobServer(port);
        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Could not start JobServer on port " + port + ": " + e.getMessage());
        }
    }
}
