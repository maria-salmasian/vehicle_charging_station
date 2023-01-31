package com.application.vehiclechargingstation.infrastructure.repository;

import com.application.vehiclechargingstation.infrastructure.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Query(value = "WITH RECURSIVE vcs(company_id, is_deleted, name, parent_company_id)\n" +
            "                   AS (\n" +
            "                   SELECT company_id, is_deleted, name, parent_company_id\n" +
            "                       FROM vehicle_charging.company\n" +
            "                       WHERE company_id = :companyId\n" +
            "                        AND company.parent_company_id IS NULL\n" +
            "                       UNION ALL\n" +
            "                       SELECT cmp.company_id, cmp.is_deleted, cmp.name, cmp.parent_company_id\n" +
            "                       FROM vehicle_charging.company cmp\n" +
            "                                INNER JOIN vehicle_charging.company cmp2 ON cmp.parent_company_id = cmp2.company_id)\n" +
            "SELECT company_id, is_deleted, name, parent_company_id\n" +
            "FROM vcs\n" +
            "ORDER BY company_id",  nativeQuery = true)
    List<Company> parentAndChildren(@Param("companyId") Integer companyId);
}



