package com.careerbridge.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * CareerBridge URL & URLConnection Demo.
 * Demonstrates:
 * 1. URL parsing (protocol, host, port, path, query parameters)
 * 2. Opening connection using URL.openConnection() / HttpURLConnection
 * 3. Setting timeouts, request methods, and headers
 * 4. Inspecting HTTP response headers and status codes
 * 5. Reading payload streams with BufferedReader
 * 6. Graceful network error handling
 *
 * RTU Syllabus: Java Networking, URL, URLConnection, HttpURLConnection,
 * HTTP Request/Response handling.
 */
public class URLConnectionDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("      CareerBridge URL & URLConnection Demo       ");
        System.out.println("==================================================\n");

        String targetUrlStr = "https://example.com/index.html?ref=careerbridge&page=1";
        if (args.length > 0) {
            targetUrlStr = args[0];
        }

        // 1. Demonstrate URL Parsing
        parseAndDisplayUrl(targetUrlStr);

        // 2. Fetch and Inspect Remote Resource via HttpURLConnection
        fetchHttpResource(targetUrlStr);
    }

    /**
     * Demonstrates parsing URL components using java.net.URI and java.net.URL.
     */
    public static void parseAndDisplayUrl(String urlString) {
        System.out.println("[Step 1] Parsing Target URL: " + urlString);
        try {
            // Using URI.toURL() as recommended in modern Java (JDK 20+)
            URL url = URI.create(urlString).toURL();
            System.out.println("   - Protocol:     " + url.getProtocol());
            System.out.println("   - Host:         " + url.getHost());
            System.out.println("   - Port:         " + (url.getPort() == -1 ? "(default)" : url.getPort()));
            System.out.println("   - Default Port: " + url.getDefaultPort());
            System.out.println("   - Path:         " + url.getPath());
            System.out.println("   - Query:        " + url.getQuery());
            System.out.println("   - File:         " + url.getFile());
            System.out.println("   - Ref/Anchor:   " + url.getRef());
            System.out.println();
        } catch (Exception e) {
            System.err.println("URL parsing failed: " + e.getMessage());
        }
    }

    /**
     * Connects to HTTP/HTTPS endpoint, inspects headers, and prints sample body content.
     */
    public static void fetchHttpResource(String urlString) {
        System.out.println("[Step 2] Connecting to Remote Endpoint: " + urlString);
        HttpURLConnection connection = null;

        try {
            URL url = URI.create(urlString).toURL();
            connection = (HttpURLConnection) url.openConnection();

            // Configure connection properties and timeouts
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5 seconds connect timeout
            connection.setReadTimeout(5000);    // 5 seconds read timeout
            connection.setRequestProperty("User-Agent", "CareerBridge-AdvancedJavaDemo/1.0 (JDK 21)");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

            System.out.println("   Connecting with 5000ms timeout...");
            connection.connect();

            int statusCode = connection.getResponseCode();
            String statusMessage = connection.getResponseMessage();
            System.out.println("   -> Response Status: " + statusCode + " (" + statusMessage + ")");
            System.out.println("   -> Content Type:    " + connection.getContentType());
            System.out.println("   -> Content Length:  " + connection.getContentLengthLong() + " bytes");
            System.out.println();

            // Display Response Headers
            System.out.println("[Step 3] Response Headers:");
            Map<String, List<String>> headers = connection.getHeaderFields();
            int headerCount = 0;
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = (entry.getKey() != null) ? entry.getKey() : "Status";
                System.out.printf("   %-20s : %s%n", key, String.join(", ", entry.getValue()));
                if (++headerCount >= 6) {
                    System.out.println("   ... (" + (headers.size() - headerCount) + " more headers omitted)");
                    break;
                }
            }
            System.out.println();

            // Read Sample Body Content (First 10 lines)
            System.out.println("[Step 4] Reading Response Body (first 10 lines):");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                int lineCount = 0;
                while ((line = reader.readLine()) != null && lineCount < 10) {
                    System.out.println("   | " + line);
                    lineCount++;
                }
                if (line != null) {
                    System.out.println("   | ... [Remaining content truncated for demo]");
                }
            }

            System.out.println("\nHTTP connection and read completed successfully!");

        } catch (IOException e) {
            System.out.println("   [Network Notice] Could not fetch remote URL: " + e.getMessage());
            System.out.println("   (This is normal in offline or restricted environments)");
            System.out.println("   The URL and URLConnection API demonstration remains fully functional.");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
