package com.application.vehiclechargingstation.ws.dto;

import lombok.Data;

@Data
public class StationDto {

    private Integer id;

    private String name;

    private Double latitude;

    private Double longitude;

    private Integer companyId;

}

