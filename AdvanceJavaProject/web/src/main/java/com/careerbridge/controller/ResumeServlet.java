package com.careerbridge.controller;

import com.careerbridge.model.ResumeMetadata;
import com.careerbridge.model.User;
import com.careerbridge.service.ResumeService;
import com.careerbridge.util.CsrfUtil;
import com.careerbridge.util.FileUploadUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@WebServlet("/candidate/resume")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB
        maxFileSize = 10 * 1024 * 1024,       // 10 MB
        maxRequestSize = 15 * 1024 * 1024     // 15 MB
)
public class ResumeServlet extends HttpServlet {
    private final ResumeService resumeService = new ResumeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("download".equalsIgnoreCase(action)) {
            handleDownload(req, resp, user);
            return;
        }

        List<ResumeMetadata> resumes = resumeService.getResumesByCandidate(user.getUserId());
        req.setAttribute("resumes", resumes);
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
        req.getRequestDispatcher("/candidate/resume.jsp").forward(req, resp);
    }

    private void handleDownload(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing resume id");
            return;
        }

        long resumeId;
        try {
            resumeId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid resume id");
            return;
        }

        Optional<ResumeMetadata> opt = resumeService.getResume(resumeId);
        if (opt.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Resume not found");
            return;
        }

        ResumeMetadata meta = opt.get();
        if (!resumeService.canAccessResume(resumeId, user.getUserId(), user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        Path filePath = Paths.get(meta.getStoragePath(), meta.getStoredFilename());
        if (!Files.exists(filePath)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found on server disk");
            return;
        }

        resp.setContentType(meta.getContentType());
        resp.setContentLengthLong(meta.getFileSize());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + meta.getOriginalFilename() + "\"");

        try (InputStream in = Files.newInputStream(filePath);
             OutputStream out = resp.getOutputStream()) {
            in.transferTo(out);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if ("delete".equalsIgnoreCase(action)) {
            try {
                long resumeId = Long.parseLong(req.getParameter("resumeId"));
                Optional<ResumeMetadata> deleted = resumeService.deleteResume(resumeId, user.getUserId());
                if (deleted.isPresent()) {
                    FileUploadUtil.deleteFile(deleted.get().getStoragePath(), deleted.get().getStoredFilename());
                }
                req.getSession().setAttribute("success", "Resume deleted.");
            } catch (Exception e) {
                req.getSession().setAttribute("error", "Failed to delete resume: " + e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/candidate/resume");
            return;
        }

        if ("setPrimary".equalsIgnoreCase(action)) {
            try {
                long resumeId = Long.parseLong(req.getParameter("resumeId"));
                resumeService.setPrimaryResume(resumeId, user.getUserId());
                req.getSession().setAttribute("success", "Primary resume updated.");
            } catch (Exception e) {
                req.getSession().setAttribute("error", "Action failed: " + e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/candidate/resume");
            return;
        }

        // File upload handling
        try {
            Part filePart = req.getPart("resumeFile");
            String validationError = FileUploadUtil.validate(filePart);
            if (validationError != null) {
                req.getSession().setAttribute("error", validationError);
                resp.sendRedirect(req.getContextPath() + "/candidate/resume");
                return;
            }

            String uploadDir = FileUploadUtil.getUploadDirectory();
            String storedFilename = FileUploadUtil.saveFile(filePart, uploadDir);

            ResumeMetadata metadata = new ResumeMetadata();
            metadata.setCandidateUserId(user.getUserId());
            metadata.setOriginalFilename(FileUploadUtil.getOriginalFilename(filePart));
            metadata.setStoredFilename(storedFilename);
            metadata.setStoragePath(uploadDir);
            metadata.setContentType(filePart.getContentType());
            metadata.setFileSize(filePart.getSize());

            List<ResumeMetadata> existing = resumeService.getResumesByCandidate(user.getUserId());
            metadata.setPrimary(existing.isEmpty());

            resumeService.uploadResume(metadata);
            req.getSession().setAttribute("success", "Resume uploaded successfully!");
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Upload failed: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/candidate/resume");
    }
}
