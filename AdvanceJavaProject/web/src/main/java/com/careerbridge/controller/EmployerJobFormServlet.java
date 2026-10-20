package com.careerbridge.controller;

import com.careerbridge.model.Job;
import com.careerbridge.model.User;
import com.careerbridge.service.JobService;
import com.careerbridge.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet("/employer/job-form")
public class EmployerJobFormServlet extends HttpServlet {
    private final JobService jobService = new JobService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isBlank()) {
            try {
                long jobId = Long.parseLong(idStr);
                Optional<Job> jobOpt = jobService.getJobById(jobId);
                if (jobOpt.isPresent() && jobService.isJobOwnedByUser(jobId, user.getUserId())) {
                    req.setAttribute("job", jobOpt.get());
                } else {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized access to job");
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }

        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
        req.getRequestDispatcher("/employer/job-form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("jobId");
        Job job = new Job();
        job.setTitle(req.getParameter("title"));
        job.setDescription(req.getParameter("description"));
        job.setRequirements(req.getParameter("requirements"));
        job.setResponsibilities(req.getParameter("responsibilities"));
        job.setLocation(req.getParameter("location"));
        job.setCurrency(req.getParameter("currency"));

        try {
            job.setJobType(Job.JobType.valueOf(req.getParameter("jobType")));
            job.setWorkMode(Job.WorkMode.valueOf(req.getParameter("workMode")));
            job.setExperienceLevel(Job.ExperienceLevel.valueOf(req.getParameter("experienceLevel")));
            job.setStatus(Job.Status.valueOf(req.getParameter("status")));

            String minSal = req.getParameter("salaryMin");
            String maxSal = req.getParameter("salaryMax");
            if (minSal != null && !minSal.isBlank()) job.setSalaryMin(new BigDecimal(minSal));
            if (maxSal != null && !maxSal.isBlank()) job.setSalaryMax(new BigDecimal(maxSal));

            String deadline = req.getParameter("applicationDeadline");
            if (deadline != null && !deadline.isBlank()) job.setApplicationDeadline(LocalDate.parse(deadline));

            if (idStr != null && !idStr.isBlank()) {
                job.setJobId(Long.parseLong(idStr));
                jobService.updateJob(job, user.getUserId());
                req.getSession().setAttribute("success", "Job updated successfully!");
            } else {
                jobService.createJob(job, user.getUserId());
                req.getSession().setAttribute("success", "Job created successfully!");
            }
            resp.sendRedirect(req.getContextPath() + "/employer/jobs");
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Failed to save job: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/employer/job-form" + (idStr != null ? "?id=" + idStr : ""));
        }
    }
}
