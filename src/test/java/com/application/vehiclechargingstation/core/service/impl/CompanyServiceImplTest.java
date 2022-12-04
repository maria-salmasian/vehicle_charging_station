package com.application.vehiclechargingstation.core.service.impl;

import com.application.vehiclechargingstation.core.model.CompanyModel;
import com.application.vehiclechargingstation.core.model.StationModel;
import com.application.vehiclechargingstation.core.service.exception.CompanyNotFoundException;
import com.application.vehiclechargingstation.infrastructure.entity.Company;
import com.application.vehiclechargingstation.infrastructure.entity.Station;
import com.application.vehiclechargingstation.infrastructure.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ModelMapper mockModelMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void getAllCompanies_ok() {
        final CompanyModel companyModel = createCompanyModel();
        final List<CompanyModel> expectedResult = Arrays.asList(companyModel);
        final Company company = createCompany();
        final List<Company> companies = Arrays.asList(company);
        when(companyRepository.findAll()).thenReturn(companies);
        final CompanyModel companyModel1 = createCompanyModel();
        when(mockModelMapper.map(company, CompanyModel.class)).thenReturn(companyModel1);
        final List<CompanyModel> result = companyService.getAllCompanies();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void getAllCompanies_empty_ok() {
        when(companyRepository.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> companyService.getAllCompanies())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getCompanyById_ok() {
        final CompanyModel expectedResult = createCompanyModel();
        final Company company1 = createCompany();
        final Optional<Company> company = Optional.of(company1);
        when(companyRepository.findById(0)).thenReturn(company);
        final CompanyModel companyModel = createCompanyModel();
        when(mockModelMapper.map(company1, CompanyModel.class)).thenReturn(companyModel);
        final CompanyModel result = companyService.getCompanyById(0);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void getCompanyById_throwsCompanyNotFoundException() {
        when(companyRepository.findById(0)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> companyService.getCompanyById(0))
                .isInstanceOf(CompanyNotFoundException.class);
    }

    @Test
    void createCompany_ok() {
        final CompanyModel companyModel = createCompanyModel();
        final Company expectedResult = createCompany();
        final Company company = createCompany();
        when(mockModelMapper.map(companyModel, Company.class)).thenReturn(company);
        final Company company1 = createCompany();
        when(companyRepository.save(company)).thenReturn(company1);
        final Company result = companyService.createCompany(companyModel);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void updateCompany_ok() {
        final CompanyModel companyModel = createCompanyModel();
        final Company expectedResult = createCompany();
        final Company company = createCompany();
        final Optional<Company> company1 = Optional.of(company);
        when(companyRepository.findById(0)).thenReturn(company1);
        final Company company2 = createCompany();
        when(companyRepository.save(company)).thenReturn(company2);
        final Company result = companyService.updateCompany(0, companyModel);

        assertThat(result).isEqualTo(expectedResult);

    }

    @Test
    void updateCompany_throwsCompanyNotFoundException() {
        final CompanyModel companyModel = createCompanyModel();
        when(companyRepository.findById(0)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> companyService.updateCompany(0, companyModel))
                .isInstanceOf(CompanyNotFoundException.class);
    }

    @Test
    void deleteCompany_ok() {
        final Company company1 = createCompany();
        final Optional<Company> company = Optional.of(company1);
        when(companyRepository.findById(0)).thenReturn(company);

        final Company company2 = new Company();
        company2.setId(0);
        company2.setName("name");
        company2.setParentCompany(1);
        company2.setDeleted(true);
        final Station station1 = new Station();
        station1.setId(0);
        station1.setName("name");
        station1.setLatitude(0.0);
        station1.setLongitude(0.0);
        station1.setDeleted(false);
        company2.setStations(Arrays.asList(station1));
        when(companyRepository.save(company1)).thenReturn(company2);
        companyService.markAsDeleted(0);

    }

    @Test
    void deleteCompany_throwsCompanyNotFoundException() {
        when(companyRepository.findById(0)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> companyService.markAsDeleted(0))
                .isInstanceOf(CompanyNotFoundException.class);
    }


    private CompanyModel createCompanyModel(){
        final CompanyModel companyModel = new CompanyModel();
        companyModel.setId(0);
        companyModel.setName("name");
        companyModel.setParentCompany(1);
        final StationModel stationModel = new StationModel();
        stationModel.setId(0);
        stationModel.setName("name");
        stationModel.setLatitude(0.0);
        stationModel.setLongitude(0.0);
        stationModel.setCompanyId(0);
        companyModel.setStations(Arrays.asList(stationModel));
        return companyModel;
    }


    private Company createCompany(){
        final Company company = new Company();
        company.setId(0);
        company.setName("name");
        company.setParentCompany(1 );
        company.setDeleted(false);
        final Station station = new Station();
        station.setId(0);
        station.setName("name");
        station.setLatitude(0.0);
        station.setLongitude(0.0);
        station.setDeleted(false);
        company.setStations(Arrays.asList(station));
        return company;
    }

}
