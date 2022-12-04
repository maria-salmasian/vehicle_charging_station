package com.application.vehiclechargingstation.core.service.impl;

import com.application.vehiclechargingstation.core.model.CompanyModel;
import com.application.vehiclechargingstation.core.service.CompanyService;
import com.application.vehiclechargingstation.core.service.exception.CompanyNotFoundException;
import com.application.vehiclechargingstation.core.service.exception.ValidationException;
import com.application.vehiclechargingstation.infrastructure.entity.Company;
import com.application.vehiclechargingstation.infrastructure.entity.Station;
import com.application.vehiclechargingstation.infrastructure.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<CompanyModel> getAllCompanies() {
        log.info("method getAllCompanies invoked from CompanyService");
        List<CompanyModel> companyModels = companyRepository
                .findAll()
                .stream()
                .map(company -> modelMapper.map(company, CompanyModel.class))
                .collect(Collectors.toList());
        Assert.notEmpty(companyModels, "No company found");
        return companyModels;
    }

    @Override
    public CompanyModel getCompanyById(Integer companyId) {
        log.info("method getCompanyById invoked from CompanyService");
        return companyRepository
                .findById(companyId)
                .map(company -> modelMapper.map(company, CompanyModel.class))
                .orElseThrow(() -> new CompanyNotFoundException("company not found for id: " + companyId));
    }

    @Override
    public Company createCompany(CompanyModel companyModel) {
        log.info("method createCompany invoked from CompanyService");
        Optional.ofNullable(companyModel).orElseThrow(() -> new ValidationException("companyModel was null"));
        Company company = companyRepository.save(modelMapper.map(companyModel, Company.class));
        Assert.state(!Objects.equals(company.getId(), company.getParentCompany()), "company parent id is the same as company id");
        return company;
    }

    @Override
    public Company updateCompany(Integer companyId, CompanyModel companyModel) {
        log.info("method updateCompany invoked from CompanyService");
        Optional.ofNullable(companyModel).orElseThrow(() -> new ValidationException("invalid companyModel"));
        Optional.ofNullable(companyId).orElseThrow(() -> new ValidationException("invalid id"));
        Assert.state(!Objects.equals(companyId, companyModel.getParentCompany()), "company parent id is the same as company id");
        return companyRepository.findById(companyId).map(company -> {
            if (!company.isDeleted()) {
                company.setName(companyModel.getName());
                company.setParentCompany(companyModel.getParentCompany());
                company.setStations(companyModel.getStations().stream().map(stationModel -> modelMapper.map(stationModel, Station.class)).collect(Collectors.toList()));
                return companyRepository.save(company);
            } else {
                throw new ValidationException("company with this id is deleted and cannot be updated: " + companyId);
            }
        }).orElseThrow(() -> new CompanyNotFoundException("company not found for id: " + companyId));
    }

    @Override
    public void markAsDeleted(Integer companyId) {
        log.info("method deleteCompany invoked from CompanyService");
        companyRepository.findById(companyId).map(company -> {
            company.setDeleted(true);
            return companyRepository.save(company);
        }).orElseThrow(() -> new CompanyNotFoundException("company not found for id: " + companyId));
    }
}

