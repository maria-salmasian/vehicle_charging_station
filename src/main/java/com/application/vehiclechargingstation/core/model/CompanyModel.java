package com.application.vehiclechargingstation.core.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
@Data
public class CompanyModel {
    private Integer id;

    private String name;

    private Integer parentCompany;

    private List<StationModel> stations = new LinkedList<>();


}
