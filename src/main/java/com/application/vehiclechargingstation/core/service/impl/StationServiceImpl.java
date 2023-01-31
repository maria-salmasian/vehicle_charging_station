package com.application.vehiclechargingstation.core.service.impl;

import com.application.vehiclechargingstation.core.model.StationModel;
import com.application.vehiclechargingstation.core.service.StationService;
import com.application.vehiclechargingstation.core.service.exception.CompanyNotFoundException;
import com.application.vehiclechargingstation.core.service.exception.StationNotFoundException;
import com.application.vehiclechargingstation.core.service.exception.ValidationException;
import com.application.vehiclechargingstation.infrastructure.entity.Company;
import com.application.vehiclechargingstation.infrastructure.entity.Station;
import com.application.vehiclechargingstation.infrastructure.repository.CompanyRepository;
import com.application.vehiclechargingstation.infrastructure.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StationServiceImpl implements StationService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<StationModel> getAllStations() {
        log.info("method getAllStations invoked from StationService");
        List<StationModel> stationModels = stationRepository
                .findAll()
                .stream()
                .filter(station -> !station.isDeleted())
                .map(company -> modelMapper.map(company, StationModel.class))
                .collect(Collectors.toList());
        Assert.notEmpty(stationModels, "No station found");
        return stationModels;
    }

    @Override
    public StationModel getStationById(Integer stationId) {
        log.info("method getStationById invoked from StationService");
        return stationRepository
                .findById(stationId)
                .map(company -> modelMapper.map(company, StationModel.class))
                .orElseThrow(() -> new StationNotFoundException("station not found for id: " + stationId));
    }

    @Override
    public Station createStation(StationModel stationModel) {
        log.info("method createStation invoked from StationService");
        Optional.ofNullable(stationModel).orElseThrow(() -> new ValidationException("stationModel was null"));
        return stationRepository.save(modelMapper.map(stationModel, Station.class));
    }

    @Override
    public Station updateStation(Integer stationId, StationModel stationModel) {
        log.info("method updateStation invoked from StationService");
        Optional.ofNullable(stationModel).orElseThrow(() -> new ValidationException("stationModel was null"));
        Optional.ofNullable(stationId).orElseThrow(() -> new ValidationException("stationId was null"));
        return stationRepository.findById(stationId).map(station -> {
            if (!station.isDeleted()) {
                station.setName(stationModel.getName());
                station.setLongitude(stationModel.getLongitude());
                station.setLatitude(stationModel.getLatitude());
                station.setCompany(companyRepository.findById(stationModel.getCompanyId()).orElseThrow(() -> new CompanyNotFoundException("company not found for station with id: " + stationId)));
                return stationRepository.save(station);
            } else {
                throw new ValidationException("station with this id is deleted and cannot be updated: " + stationId);
            }
        }).orElseThrow(() -> new StationNotFoundException("station not found for id: " + stationId));
    }

    @Override
    public void deleteStation(Integer stationId) {
        log.info("method deleteStation invoked from StationService");
        stationRepository.findById(stationId).map(station -> {
            station.setDeleted(true);
            return stationRepository.save(station);
        }).orElseThrow(() -> new StationNotFoundException("station not found for id: " + stationId));

    }


    public List<StationModel> stationsWithinRadius(Double radius, Double longitude, Double latitude) {
        return stationRepository.findAllWithinRadiusSortedByDistance(radius, latitude, longitude)
                .stream()
                .filter(station -> !station.isDeleted())
                .map(station -> modelMapper.map(station, StationModel.class))
                .collect(Collectors.toList());
    }



    public List<Station> getAllChildCompaniesStations(Integer companyId) {
        log.info("method getAllChildCompaniesStations invoked from CompanyService");
        List<Station> stations =new ArrayList<>();
        List<Company> parentAndChildren = companyRepository.parentAndChildren(companyId);
        parentAndChildren.forEach(c -> stations.addAll(c.getStations()));

        return stations;
    }
}
