package com.medhead.appGestion.repository;

import com.medhead.appGestion.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Boolean existsByName(String name);
    Hospital findHospitalByName(String name);
}
