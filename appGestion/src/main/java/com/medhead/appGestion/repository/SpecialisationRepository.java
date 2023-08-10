package com.medhead.appGestion.repository;


import com.medhead.appGestion.model.Specialisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialisationRepository extends JpaRepository<Specialisation, Long> {
    Boolean existsByName(String name);
    Specialisation  getSpecialisationByName(String name);
}
