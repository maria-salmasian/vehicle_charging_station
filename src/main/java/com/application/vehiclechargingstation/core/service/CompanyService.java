package com.application.vehiclechargingstation.core.service;

import com.application.vehiclechargingstation.core.model.CompanyModel;
import com.application.vehiclechargingstation.infrastructure.entity.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {
    List<CompanyModel> getAllCompanies();
    CompanyModel getCompanyById(Integer companyId);
    Company createCompany(CompanyModel companyModel);
    Company updateCompany(Integer companyId, CompanyModel companyModel);
    void markAsDeleted(Integer companyId);
}
