package com.medhead.appGestion.repository;

import com.medhead.appGestion.model.HospitalSpecialisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalSpecialisationRepository  extends JpaRepository<HospitalSpecialisation, Long> {
    @Query("SELECT t FROM HospitalSpecialisation t WHERE t.hospital.id = ?1 and t.specialisation.id = ?2")
    HospitalSpecialisation checkIfLinkIsAlready(long hospitalId, long SpecialisationId);

    List <HospitalSpecialisation> findBySpecialisationName(String name);


}
