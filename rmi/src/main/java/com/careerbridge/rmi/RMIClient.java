package com.careerbridge.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

/**
 * CareerBridge RMI Client Demo.
 * Connects to the RMI registry and invokes remote query methods on JobSearchService.
 */
public class RMIClient {
    public static void main(String[] args) {
        try {
            System.out.println("Connecting to CareerBridge RMI Server at localhost:1099...");
            Registry registry = LocateRegistry.getRegistry("localhost", RMIServer.REGISTRY_PORT);
            JobSearchService service = (JobSearchService) registry.lookup(RMIServer.SERVICE_NAME);

            System.out.println("\n=== 1. Searching for 'Java' jobs via RMI ===");
            List<String> javaJobs = service.searchJobs("Java");
            for (String job : javaJobs) {
                System.out.println(" - " + job);
            }

            System.out.println("\n=== 2. Fetching detailed info for Job #1 via RMI ===");
            String details = service.getJobDetails(1);
            System.out.println(details);

            System.out.println("\n=== 3. Querying listing statistics via RMI ===");
            Map<String, Integer> stats = service.getStatistics();
            stats.forEach((status, count) -> System.out.println(status + ": " + count));

            System.out.println("\nAll RMI remote invocations completed successfully!");
        } catch (Exception e) {
            System.err.println("RMI Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
