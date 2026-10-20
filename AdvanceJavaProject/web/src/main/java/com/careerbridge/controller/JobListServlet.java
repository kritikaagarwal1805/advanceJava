package com.careerbridge.controller;

import com.careerbridge.model.*;
import com.careerbridge.service.JobService;
import com.careerbridge.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/jobs")
public class JobListServlet extends HttpServlet {
    private final JobService jobService = new JobService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JobSearchCriteria criteria = new JobSearchCriteria();
        criteria.setKeyword(req.getParameter("keyword"));
        criteria.setLocation(req.getParameter("location"));
        criteria.setJobType(req.getParameter("jobType"));
        criteria.setExperienceLevel(req.getParameter("experienceLevel"));
        criteria.setWorkMode(req.getParameter("workMode"));
        criteria.setStatus("PUBLISHED");

        String pageStr = req.getParameter("page");
        int page = 1;
        try { if (pageStr != null) page = Math.max(1, Integer.parseInt(pageStr)); }
        catch (NumberFormatException ignored) {}
        criteria.setPage(page);
        criteria.setPageSize(9);

        List<Job> jobs = jobService.searchJobs(criteria);
        int totalResults = jobService.countSearchResults(criteria);
        int totalPages = (int) Math.ceil((double) totalResults / criteria.getPageSize());

        req.setAttribute("jobs", jobs);
        req.setAttribute("criteria", criteria);
        req.setAttribute("totalResults", totalResults);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/jobs.jsp").forward(req, resp);
    }
}
