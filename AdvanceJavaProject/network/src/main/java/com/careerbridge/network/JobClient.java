package com.careerbridge.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * CareerBridge Socket Client Demo.
 * Connects to JobServer on localhost:5000 via TCP socket.
 * Supports both an interactive CLI shell and an automated demo mode.
 *
 * RTU Syllabus: Socket Programming, Client Socket, Stream I/O, Network Protocols.
 */
public class JobClient {

    private final String host;
    private final int port;

    public JobClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Executes the automated demonstration sequence of predefined commands.
     */
    public void runDemo() {
        System.out.println("==================================================");
        System.out.println("     CareerBridge Socket Client - Demo Mode       ");
        System.out.println("==================================================");
        System.out.println("Connecting to server at " + host + ":" + port + "...\n");

        try (
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Read initial greeting lines
            System.out.println("<<< " + in.readLine());
            System.out.println("<<< " + in.readLine());
            System.out.println();

            String[] testCommands = {
                    "GET_STATS",
                    "SEARCH_JOBS Java",
                    "SEARCH_JOBS Cloud",
                    "GET_JOB 101",
                    "GET_JOB 999",       // Test job not found
                    "INVALID_TEST_CMD",  // Test malformed command
                    "QUIT"
            };

            for (String cmd : testCommands) {
                System.out.println(">>> Sending command: " + cmd);
                out.println(cmd);

                // Read response
                readResponse(cmd, in);
                System.out.println("--------------------------------------------------");
                Thread.sleep(400); // Small pause for readable demo output
            }

            System.out.println("\nDemo sequence completed successfully!");

        } catch (IOException e) {
            System.err.println("Communication error with server: " + e.getMessage());
            System.err.println("Ensure JobServer is running on " + host + ":" + port);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Enters an interactive console prompt for manual command entry.
     */
    public void runInteractive() {
        System.out.println("==================================================");
        System.out.println("  CareerBridge Socket Client - Interactive Mode   ");
        System.out.println("==================================================");
        System.out.println("Connecting to " + host + ":" + port + "...");

        try (
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)
        ) {
            // Read greeting
            System.out.println(in.readLine());
            System.out.println(in.readLine());
            System.out.println("Type 'QUIT' to exit.\n");

            while (true) {
                System.out.print("CareerBridge> ");
                if (!scanner.hasNextLine()) break;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                out.println(line);
                readResponse(line, in);

                if ("QUIT".equalsIgnoreCase(line)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Connection failed: " + e.getMessage());
            System.err.println("Ensure JobServer is running on " + host + ":" + port);
        }
    }

    /**
     * Reads responses from server, handling multi-line responses for SEARCH_JOBS.
     */
    private void readResponse(String cmd, BufferedReader in) throws IOException {
        String line = in.readLine();
        if (line == null) {
            System.out.println("<<< [Server closed connection]");
            return;
        }

        System.out.println("<<< " + line);

        // Multi-line reading for SEARCH_JOBS until END
        if (cmd.toUpperCase().startsWith("SEARCH_JOBS")) {
            while ((line = in.readLine()) != null) {
                if ("END".equals(line)) {
                    break;
                }
                System.out.println("    " + line);
            }
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = JobServer.DEFAULT_PORT;
        boolean interactive = false;

        for (String arg : args) {
            if ("--interactive".equalsIgnoreCase(arg) || "-i".equalsIgnoreCase(arg)) {
                interactive = true;
            } else if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.substring(7));
            } else if (arg.startsWith("--host=")) {
                host = arg.substring(7);
            }
        }

        JobClient client = new JobClient(host, port);
        if (interactive) {
            client.runInteractive();
        } else {
            client.runDemo();
        }
    }
}
