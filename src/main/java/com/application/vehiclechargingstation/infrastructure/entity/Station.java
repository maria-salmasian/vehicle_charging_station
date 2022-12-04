package com.application.vehiclechargingstation.infrastructure.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "station", schema = "vehicle_charging")
public class Station {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Integer id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private boolean isDeleted;

}
