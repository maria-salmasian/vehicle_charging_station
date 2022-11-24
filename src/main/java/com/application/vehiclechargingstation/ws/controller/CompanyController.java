package com.application.vehiclechargingstation.ws.controller;

import com.application.vehiclechargingstation.core.model.CompanyModel;
import com.application.vehiclechargingstation.core.service.CompanyService;
import com.application.vehiclechargingstation.ws.dto.CompanyDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController()
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        log.info("method getAllTodoItems invoked from CompanyController");
        List<CompanyDto> companyDtos = companyService.getAllCompanies().stream().map(companyModel -> modelMapper.map(companyModel, CompanyDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(companyDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable int id) {
        log.info("method getCompanyById invoked from CompanyController");
        CompanyDto companyDto = modelMapper.map(companyService.getCompanyById(id), CompanyDto.class);
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createCompany(@RequestBody CompanyDto companyDto) {
        Assert.notNull(companyDto, "companyDto body is null");
        log.info("method createCompany invoked from CompanyController");
        companyService.createCompany(modelMapper.map(companyDto, CompanyModel.class));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@RequestBody @Valid CompanyDto companyDto, @PathVariable int id) {
        Assert.notNull(companyDto, "body is null");
        log.info("method updateCompany invoked from CompanyController");
        companyService.updateCompany(id, modelMapper.map(companyDto, CompanyModel.class));
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable int id) {
        log.info("method deleteCompany invoked from CompanyController");
        companyService.markAsDeleted(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}

