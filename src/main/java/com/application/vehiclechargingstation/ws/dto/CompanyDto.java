package com.application.vehiclechargingstation.ws.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
@Data
public class CompanyDto {
    private Integer id;

    private String name;

    private Integer parentCompany;

    private List<StationDto> stations = new LinkedList<>();
}
