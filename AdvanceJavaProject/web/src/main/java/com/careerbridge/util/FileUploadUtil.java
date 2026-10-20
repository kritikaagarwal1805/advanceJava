package com.careerbridge.util;

import jakarta.servlet.http.Part;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Safe file upload handling for resumes.
 * Validates file type, size, and generates safe stored filenames.
 */
public class FileUploadUtil {
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "docx");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
        "application/pdf",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    // PDF magic bytes: %PDF
    private static final byte[] PDF_SIGNATURE = {0x25, 0x50, 0x44, 0x46};
    // DOCX (ZIP) magic bytes: PK
    private static final byte[] DOCX_SIGNATURE = {0x50, 0x4B, 0x03, 0x04};
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    
    private FileUploadUtil() {}

    /**
     * Validate an uploaded file part.
     * @return error message, or null if valid
     */
    public static String validate(Part filePart) {
        if (filePart == null || filePart.getSize() == 0) {
            return "No file uploaded.";
        }
        if (filePart.getSize() > MAX_FILE_SIZE) {
            return "File size exceeds 10 MB limit.";
        }
        String filename = getOriginalFilename(filePart);
        if (filename == null || filename.isEmpty()) {
            return "Invalid filename.";
        }
        String ext = getExtension(filename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            return "Only PDF and DOCX files are allowed.";
        }
        String mimeType = filePart.getContentType();
        if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            return "Invalid file type.";
        }
        return null; // valid
    }

    /**
     * Validate file signature (magic bytes) for extra security.
     */
    public static boolean validateFileSignature(InputStream is, String extension) throws IOException {
        byte[] header = new byte[4];
        int bytesRead = is.read(header);
        if (bytesRead < 4) return false;
        
        return switch (extension.toLowerCase()) {
            case "pdf" -> Arrays.equals(header, PDF_SIGNATURE);
            case "docx" -> Arrays.equals(header, DOCX_SIGNATURE);
            default -> false;
        };
    }

    /**
     * Generate a safe stored filename using UUID.
     */
    public static String generateStoredFilename(String originalFilename) {
        String ext = getExtension(originalFilename);
        return UUID.randomUUID().toString() + "." + ext.toLowerCase();
    }

    /**
     * Save uploaded file to the specified directory.
     * Creates directory if it doesn't exist.
     * @return the stored filename
     */
    public static String saveFile(Part filePart, String uploadDir) throws IOException {
        String originalFilename = getOriginalFilename(filePart);
        String storedFilename = generateStoredFilename(originalFilename);
        
        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        // Prevent path traversal
        Path filePath = dirPath.resolve(storedFilename).normalize();
        if (!filePath.startsWith(dirPath)) {
            throw new SecurityException("Path traversal attempt detected");
        }
        
        try (InputStream is = filePart.getInputStream();
             OutputStream os = Files.newOutputStream(filePath)) {
            is.transferTo(os);
        }
        
        return storedFilename;
    }

    /**
     * Delete a stored file.
     */
    public static boolean deleteFile(String uploadDir, String storedFilename) {
        try {
            Path filePath = Paths.get(uploadDir, storedFilename).normalize();
            Path dirPath = Paths.get(uploadDir);
            if (!filePath.startsWith(dirPath)) return false;
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }

    public static String getOriginalFilename(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) return null;
        for (String token : header.split(";")) {
            if (token.trim().startsWith("filename")) {
                String name = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                // Extract just the filename, strip any path
                int lastSlash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
                return lastSlash >= 0 ? name.substring(lastSlash + 1) : name;
            }
        }
        return null;
    }

    public static String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot + 1) : "";
    }

    public static String getUploadDirectory() {
        // Default upload directory outside web root
        String dir = System.getProperty("careerbridge.upload.dir");
        if (dir != null) return dir;
        Properties props = new Properties();
        try (InputStream is = FileUploadUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
                dir = props.getProperty("upload.directory");
                if (dir != null) return dir;
            }
        } catch (IOException ignored) {}
        // Fallback
        return System.getProperty("user.home") + File.separator + "careerbridge" + File.separator + "uploads";
    }
}
