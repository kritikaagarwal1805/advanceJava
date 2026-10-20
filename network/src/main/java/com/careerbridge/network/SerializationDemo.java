package com.careerbridge.network;

import java.io.*;

/**
 * CareerBridge Object Serialization Demo.
 * Demonstrates:
 * 1. Object serialization to disk (ObjectOutputStream / FileOutputStream)
 * 2. Object deserialization from disk (ObjectInputStream / FileInputStream)
 * 3. The role and significance of serialVersionUID for class versioning
 * 4. Handling of transient fields during serialization
 *
 * RTU Syllabus: Java Object Serialization, Serializable Interface,
 * ObjectOutputStream, ObjectInputStream, serialVersionUID.
 */
public class SerializationDemo {

    private static final String DATA_FILE = "job_data.ser";

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("      CareerBridge Object Serialization Demo      ");
        System.out.println("==================================================\n");

        // 1. Create a sample JobSummary instance
        JobSummary originalJob = new JobSummary(
                501L,
                "Lead Cloud Architect",
                "CareerBridge Global Technologies",
                "Jaipur / Remote",
                "Full-time",
                "Published"
        );

        System.out.println("[Step 1] Created original JobSummary object in memory:");
        System.out.println("   " + originalJob);
        System.out.println("   Object HashCode: " + System.identityHashCode(originalJob));
        System.out.println();

        // 2. Serialize the object to a binary file
        System.out.println("[Step 2] Serializing object to file: " + DATA_FILE);
        serializeJob(originalJob, DATA_FILE);

        // 3. Inspect serialized file properties
        File file = new File(DATA_FILE);
        if (file.exists()) {
            System.out.println("   Successfully written! File size on disk: " + file.length() + " bytes");
        }
        System.out.println();

        // 4. Deserialize the object from the file
        System.out.println("[Step 3] Deserializing object from file: " + DATA_FILE);
        JobSummary restoredJob = deserializeJob(DATA_FILE);

        if (restoredJob != null) {
            System.out.println("   Successfully restored object state:");
            System.out.println("   " + restoredJob);
            System.out.println("   Restored Object HashCode: " + System.identityHashCode(restoredJob));
            System.out.println("   Objects are equal in state: " +
                    (originalJob.getId() == restoredJob.getId() &&
                     originalJob.getTitle().equals(restoredJob.getTitle())));
        }
        System.out.println();

        // 5. Educational explanation of serialVersionUID
        printSerializationConcepts();

        // Cleanup temporary serialization artifact
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("[Cleanup] Temporary file '" + DATA_FILE + "' removed: " + deleted);
        }
    }

    /**
     * Serializes a JobSummary object to the given file path.
     */
    public static void serializeJob(JobSummary job, String filePath) {
        try (
                FileOutputStream fos = new FileOutputStream(filePath);
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(job);
            oos.flush();
            System.out.println("   -> ObjectOutputStream successfully persisted graph.");
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
        }
    }

    /**
     * Deserializes a JobSummary object from the given file path.
     */
    public static JobSummary deserializeJob(String filePath) {
        try (
                FileInputStream fis = new FileInputStream(filePath);
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object obj = ois.readObject();
            if (obj instanceof JobSummary) {
                return (JobSummary) obj;
            } else {
                System.err.println("Unexpected object type in stream: " + obj.getClass());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("I/O error during deserialization: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class definition not found: " + e.getMessage());
        }
        return null;
    }

    /**
     * Educational summary of serialization mechanisms.
     */
    private static void printSerializationConcepts() {
        System.out.println("================ Educational Takeaways ================");
        System.out.println("1. java.io.Serializable is a marker interface (no methods).");
        System.out.println("2. serialVersionUID ensures sender and receiver agree on class");
        System.out.println("   definitions. If the class structure changes without updating");
        System.out.println("   the UID, an InvalidClassException is thrown.");
        System.out.println("3. 'transient' modifier excludes sensitive or non-serializable");
        System.out.println("   fields (like passwords, DB connections, or open streams).");
        System.out.println("4. Custom serialization behavior can be defined by implementing");
        System.out.println("   private void writeObject() and readObject() methods.");
        System.out.println("=======================================================");
    }
}
