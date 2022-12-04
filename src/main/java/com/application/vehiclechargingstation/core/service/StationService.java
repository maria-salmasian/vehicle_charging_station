package com.application.vehiclechargingstation.core.service;

import com.application.vehiclechargingstation.core.model.StationModel;
import com.application.vehiclechargingstation.infrastructure.entity.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StationService {
    List<StationModel> getAllStations();
    StationModel getStationById(Integer stationId);
    Station createStation(StationModel stationModel);
    Station updateStation(Integer stationId, StationModel stationModel);
    void deleteStation(Integer stationId);
    List<StationModel> stationsWithinRadius(Double radius, Double longitude, Double latitude);
    List<Station> getAllChildCompaniesStations(Integer companyId);
}