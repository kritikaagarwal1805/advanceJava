package com.careerbridge.service;

import com.careerbridge.dao.CompanyDAO;
import com.careerbridge.model.Company;
import com.careerbridge.util.ValidationUtil;
import java.sql.SQLException;
import java.util.*;

/**
 * Company profile management service.
 */
public class CompanyService {
    private final CompanyDAO companyDAO = new CompanyDAO();

    public Company createCompany(Company company) {
        if (ValidationUtil.isNullOrEmpty(company.getName()))
            throw new ServiceException("Company name is required.");
        try {
            companyDAO.create(company);
            return company;
        } catch (SQLException e) { throw new ServiceException("Failed to create company.", e); }
    }

    public Optional<Company> getCompanyById(long companyId) {
        try { return companyDAO.findById(companyId); }
        catch (SQLException e) { throw new ServiceException("Failed to load company.", e); }
    }

    public Optional<Company> getCompanyByOwner(long ownerUserId) {
        try { return companyDAO.findByOwnerUserId(ownerUserId); }
        catch (SQLException e) { throw new ServiceException("Failed to load company.", e); }
    }

    public boolean updateCompany(Company company, long ownerUserId) {
        if (company.getOwnerUserId() != ownerUserId)
            throw new ServiceException("You can only edit your own company.");
        try { return companyDAO.update(company); }
        catch (SQLException e) { throw new ServiceException("Failed to update company.", e); }
    }

    public List<Company> getAllCompanies(int page, int pageSize) {
        try { return companyDAO.findAll(page, pageSize); }
        catch (SQLException e) { throw new ServiceException("Failed to load companies.", e); }
    }

    public int getCompanyCount() {
        try { return companyDAO.count(); }
        catch (SQLException e) { throw new ServiceException("Failed to count companies.", e); }
    }

    public boolean updateCompanyStatus(long companyId, Company.Status status) {
        try { return companyDAO.updateStatus(companyId, status); }
        catch (SQLException e) { throw new ServiceException("Failed to update company status.", e); }
    }
}
