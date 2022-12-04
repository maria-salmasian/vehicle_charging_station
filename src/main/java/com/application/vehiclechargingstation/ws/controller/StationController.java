package com.application.vehiclechargingstation.ws.controller;

import com.application.vehiclechargingstation.core.model.StationModel;
import com.application.vehiclechargingstation.core.service.StationService;
import com.application.vehiclechargingstation.ws.dto.StationDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController()
@RequestMapping("/station")
public class StationController {

    @Autowired
    private StationService stationService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<StationDto>> getAllStations() {
        log.info("method getAllTodoItems invoked from StationController");
        List<StationDto> stationDtos = stationService.getAllStations().stream().map(stationModel -> modelMapper.map(stationModel, StationDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(stationDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationDto> getStationById(@PathVariable int id) {
        log.info("method getStationById invoked from StationController");
        StationDto stationDto = modelMapper.map(stationService.getStationById(id), StationDto.class);
        return new ResponseEntity<>(stationDto, HttpStatus.OK);
    }


    @GetMapping("/withinRadius")
    public ResponseEntity<List<StationDto>> getStationWithinRadius(@RequestParam double radius, @RequestParam double latitude, @RequestParam double longitude) {
        log.info("method getStationWithinRadius invoked from StationController");
        List<StationDto> stationDtos = stationService.stationsWithinRadius(radius, longitude, latitude).stream().map(stationModel -> modelMapper.map(stationModel, StationDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(stationDtos, HttpStatus.OK);
    }

    @GetMapping("/withChildStations/{companyId}")
    public ResponseEntity<List<StationDto>> getStationWithChildren(@PathVariable Integer companyId ) {
        log.info("method getStationWithChildren invoked from StationController");
        List<StationDto> stationDtos = stationService.getAllChildCompaniesStations(companyId).stream().map(stationModel -> modelMapper.map(stationModel, StationDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(stationDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createStation(@RequestBody StationDto stationDto) {
        Assert.notNull(stationDto, "stationDto body is null");
        log.info("method createStation invoked from StationController");
        stationService.createStation(modelMapper.map(stationDto, StationModel.class));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStation(@RequestBody StationDto stationDto, @PathVariable int id) {
        Assert.notNull(stationDto, "body is null");
        log.info("method updateStation invoked from StationController");
        stationService.updateStation(id, modelMapper.map(stationDto, StationModel.class));
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStation(@PathVariable int id) {
        log.info("method deleteStation invoked from StationController");
        stationService.deleteStation(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}

