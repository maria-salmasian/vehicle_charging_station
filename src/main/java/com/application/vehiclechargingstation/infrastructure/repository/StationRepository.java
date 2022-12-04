package com.application.vehiclechargingstation.infrastructure.repository;

import com.application.vehiclechargingstation.infrastructure.entity.Station;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
       @Query(value = "SELECT station_id,\n" +
            "       name,\n" +
            "       latitude,\n" +
            "       longitude,\n" +
            "       company_id,\n" +
            "       point_distance(point(:lt , :lng), point(latitude, longitude)) as dist,\n" +
            "       is_deleted\n" +
            "FROM vehicle_charging.station\n" +
            "where latitude <= :lt + :r and longitude <= :lng + :r\n" +
            "order by dist asc", nativeQuery = true)
    List<Station> findAllWithinRadiusSortedByDistance(@Param("r") Double radius, @Param("lt") Double lt, @Param("lng") Double lng);


}
