package com.careerbridge.controller;

import com.careerbridge.model.Company;
import com.careerbridge.model.User;
import com.careerbridge.service.CompanyService;
import com.careerbridge.util.CsrfUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/employer/company")
public class CompanyProfileServlet extends HttpServlet {
    private final CompanyService companyService = new CompanyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Optional<Company> company = companyService.getCompanyByOwner(user.getUserId());
        company.ifPresent(c -> req.setAttribute("company", c));
        req.setAttribute("csrfToken", CsrfUtil.getToken(req.getSession()));
        req.getRequestDispatcher("/employer/company.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Company company = new Company();
        company.setOwnerUserId(user.getUserId());
        company.setName(req.getParameter("name"));
        company.setDescription(req.getParameter("description"));
        company.setWebsite(req.getParameter("website"));
        company.setIndustry(req.getParameter("industry"));
        company.setLocation(req.getParameter("location"));
        company.setLogoUrl(req.getParameter("logoUrl"));

        try {
            Optional<Company> existing = companyService.getCompanyByOwner(user.getUserId());
            if (existing.isPresent()) {
                company.setCompanyId(existing.get().getCompanyId());
                companyService.updateCompany(company, user.getUserId());
                req.getSession().setAttribute("success", "Company profile updated successfully!");
            } else {
                companyService.createCompany(company);
                req.getSession().setAttribute("success", "Company profile created successfully!");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Error saving company: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/employer/company");
    }
}
