package com.careerbridge.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Remote Interface defining the CareerBridge RMI Job Search service contract.
 *
 * RTU Syllabus: Java Remote Method Invocation (RMI), Remote Interface,
 * RemoteException, Distributed Objects.
 */
public interface JobSearchService extends Remote {

    /**
     * Searches for job listings matching the provided keyword.
     *
     * @param keyword query string to search across title, company, or skills
     * @return list of matching job summary strings
     * @throws RemoteException if RMI transport fails
     */
    List<String> searchJobs(String keyword) throws RemoteException;

    /**
     * Retrieves detailed information for a specific job posting.
     *
     * @param jobId the unique identifier of the job
     * @return formatted string containing job details, or null if not found
     * @throws RemoteException if RMI transport fails
     */
    String getJobDetails(int jobId) throws RemoteException;

    /**
     * Computes aggregate job statistics grouped by status.
     *
     * @return map of status name to count
     * @throws RemoteException if RMI transport fails
     */
    Map<String, Integer> getStatistics() throws RemoteException;
}
