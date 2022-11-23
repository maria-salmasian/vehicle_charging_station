package com.application.vehiclechargingstation.infrastructure.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Table(name = "company", schema = "vehicle_charging")
public class Company {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "parent_company_id")
    private Integer parentCompany;

    @Column
    private boolean isDeleted;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Station> stations = new LinkedList<>();



}
