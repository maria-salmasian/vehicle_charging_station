package com.application.vehiclechargingstation.core.service.impl;

import com.application.vehiclechargingstation.core.model.StationModel;
import com.application.vehiclechargingstation.core.service.exception.CompanyNotFoundException;
import com.application.vehiclechargingstation.core.service.exception.StationNotFoundException;
import com.application.vehiclechargingstation.infrastructure.entity.Company;
import com.application.vehiclechargingstation.infrastructure.entity.Station;
import com.application.vehiclechargingstation.infrastructure.repository.CompanyRepository;
import com.application.vehiclechargingstation.infrastructure.repository.StationRepository;
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
class StationServiceImplTest {

    @Mock
    private StationRepository stationRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StationServiceImpl stationService;

    @Test
    void getAllStations_ok() {
        final StationModel stationModel = createStationModel();
        final List<StationModel> expectedResult = Arrays.asList(stationModel);
        final Station station = createStation();
        final List<Station> stations = Arrays.asList(station);
        when(stationRepository.findAll()).thenReturn(stations);
        when(modelMapper.map(station, StationModel.class)).thenReturn(stationModel);

        final List<StationModel> result = stationService.getAllStations();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void getAllStations_Empty_ok() {
        when(stationRepository.findAll()).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> stationService.getAllStations())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getStationById_ok() {
        final StationModel expectedResult = createStationModel();
        final Station station1 = createStation();
        final Optional<Station> station = Optional.of(station1);
        when(stationRepository.findById(0)).thenReturn(station);
        final StationModel stationModel = createStationModel();
        when(modelMapper.map(station1, StationModel.class)).thenReturn(stationModel);
        final StationModel result = stationService.getStationById(0);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void getStationById_throwsException() {
        when(stationRepository.findById(0)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> stationService.getStationById(0))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    void createStation_ok() {
        final StationModel stationModel = createStationModel();
        final Station expectedResult = createStation();
        final Station station = createStation();
        when(modelMapper.map(stationModel, Station.class)).thenReturn(station);
        final Station station1 = createStation();
        when(stationRepository.save(station)).thenReturn(station1);
        final Station result = stationService.createStation(stationModel);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void updateStation_ok() {
        final StationModel stationModel = createStationModel();
        final Station expectedResult = createStation();
        final Station station1 = createStation();
        final Optional<Station> station = Optional.of(station1);
        when(stationRepository.findById(0)).thenReturn(station);

        final Company company3 = new Company();
        company3.setId(0);
        company3.setName("name");
        company3.setParentCompany(0);
        company3.setDeleted(false);
        final Station station2 = createStation();
        company3.setStations(Arrays.asList(station2));
        when(companyRepository.findById(0)).thenReturn(Optional.of(company3));
        when(stationRepository.save(station1)).thenReturn(station2);
        final Station result = stationService.updateStation(0, stationModel);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void updateStation_throwsStationNotFoundException() {
        final StationModel stationModel = createStationModel();
        when(stationRepository.findById(0)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> stationService.updateStation(0, stationModel))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    void updateStation_throwsCompanyNotFoundException() {
        final StationModel stationModel = createStationModel();
        final Station station1 = createStation();
        final Optional<Station> station = Optional.of(station1);
        when(stationRepository.findById(0)).thenReturn(station);
        when(companyRepository.findById(0)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> stationService.updateStation(0, stationModel))
                .isInstanceOf(CompanyNotFoundException.class);
    }

    @Test
    void deleteStation_ok() {
        final Station station1 = createStation();
        final Optional<Station> station = Optional.of(station1);
        when(stationRepository.findById(0)).thenReturn(station);

        final Station station2 = new Station();
        station2.setId(0);
        station2.setName("name");
        station2.setLatitude(0.0);
        station2.setLongitude(0.0);
        final Company company1 = new Company();
        company1.setId(0);
        company1.setName("name");
        company1.setParentCompany(0);
        company1.setDeleted(false);
        company1.setStations(Arrays.asList(new Station()));
        station2.setCompany(company1);
        station2.setDeleted(true);
        when(stationRepository.save(station1)).thenReturn(station2);

        stationService.deleteStation(0);
    }

    @Test
    void deleteStation_throwsStationNotFoundException() {
        when(stationRepository.findById(0)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> stationService.deleteStation(0))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    void stationWithinRadius_ok() {
        final StationModel stationModel = createStationModel();
        final List<StationModel> expectedResult = Arrays.asList(stationModel);
        final Station station = createStation();
        final List<Station> stations = Arrays.asList(station);
        when(stationRepository.findAllWithinRadiusSortedByDistance(0.0, 0.0, 0.0)).thenReturn(stations);
        final StationModel stationModel1 = createStationModel();
        when(modelMapper.map(station, StationModel.class)).thenReturn(stationModel1);
        final List<StationModel> result = stationService.stationsWithinRadius(0.0, 0.0, 0.0);
        assertThat(result).isEqualTo(expectedResult);
    }

    private Station createStation() {
        final Station station = new Station();
        station.setId(0);
        station.setName("name");
        station.setLatitude(0.0);
        station.setLongitude(0.0);
        final Company company = new Company();
        company.setId(0);
        company.setName("name");
        company.setParentCompany(0);
        company.setDeleted(false);
        company.setStations(Arrays.asList(new Station()));
        station.setCompany(company);
        station.setDeleted(false);
        final List<Station> stations = Arrays.asList(station);
        return station;
    }

    private StationModel createStationModel() {
        StationModel stationModel = new StationModel();
        stationModel.setId(0);
        stationModel.setName("name");
        stationModel.setLatitude(0.0);
        stationModel.setLongitude(0.0);
        stationModel.setCompanyId(0);
        return stationModel;
    }


}
