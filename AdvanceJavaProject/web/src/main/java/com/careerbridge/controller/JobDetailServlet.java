package com.careerbridge.controller;

import com.careerbridge.model.Job;
import com.careerbridge.model.JobApplication;
import com.careerbridge.model.ResumeMetadata;
import com.careerbridge.model.User;
import com.careerbridge.service.ApplicationService;
import com.careerbridge.service.CompanyService;
import com.careerbridge.service.JobService;
import com.careerbridge.service.ResumeService;
import com.careerbridge.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/job-details")
public class JobDetailServlet extends HttpServlet {
    private final JobService jobService = new JobService();
    private final CompanyService companyService = new CompanyService();
    private final ApplicationService applicationService = new ApplicationService();
    private final ResumeService resumeService = new ResumeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
            return;
        }

        try {
            long jobId = Long.parseLong(idParam);
            Optional<Job> jobOpt = jobService.getJobById(jobId);
            if (jobOpt.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Job not found");
                return;
            }

            Job job = jobOpt.get();
            req.setAttribute("job", job);
            companyService.getCompanyById(job.getCompanyId()).ifPresent(c -> req.setAttribute("company", c));

            HttpSession session = req.getSession(false);
            boolean hasApplied = false;
            boolean canApply = false;

            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                if (user.getRole() == User.Role.JOB_SEEKER) {
                    canApply = applicationService.canCandidateApply(jobId, user.getUserId());
                    hasApplied = !canApply;
                    List<ResumeMetadata> resumes = resumeService.getResumesByCandidate(user.getUserId());
                    req.setAttribute("resumes", resumes);
                }
            }

            req.setAttribute("hasApplied", hasApplied);
            req.setAttribute("canApply", canApply && job.isOpen());
            req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
            req.getRequestDispatcher("/job-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/jobs");
        }
    }
}
