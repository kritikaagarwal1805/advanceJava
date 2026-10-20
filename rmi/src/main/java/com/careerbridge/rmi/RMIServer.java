package com.careerbridge.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * CareerBridge RMI Server Launcher.
 * Starts an in-process RMI registry on port 1099 and exports JobSearchService.
 */
public class RMIServer {
    public static final int REGISTRY_PORT = 1099;
    public static final String SERVICE_NAME = "JobSearchService";

    public static void main(String[] args) {
        try {
            System.out.println("Starting CareerBridge RMI Server...");
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(REGISTRY_PORT);
                System.out.println("Created new RMI Registry on port " + REGISTRY_PORT);
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(REGISTRY_PORT);
                System.out.println("Reusing existing RMI Registry on port " + REGISTRY_PORT);
            }

            JobSearchServiceImpl service = new JobSearchServiceImpl();
            registry.rebind(SERVICE_NAME, service);
            System.out.println("CareerBridge JobSearchService successfully bound to registry.");
            System.out.println("RMI Server is ready for remote queries.");
        } catch (Exception e) {
            System.err.println("RMI Server failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
