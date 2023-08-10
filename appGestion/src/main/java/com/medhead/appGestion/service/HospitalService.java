package com.medhead.appGestion.service;

import com.medhead.appGestion.model.Hospital;
import com.medhead.appGestion.repository.HospitalRepository;
import com.medhead.appGestion.repository.SpecialisationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private SpecialisationRepository specialisationRepository;


    @Transactional
    public Hospital addHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }
    @Transactional
    public ResponseEntity<String> deleteHospitalById(Long id) {
        if(hospitalRepository.existsById(id)) {
            hospitalRepository.deleteById(id);
            return ResponseEntity.ok("Suppression réussie");
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Suppression invalides");
        }
    }

    @Transactional
    public List<Hospital> getAllHospital(){
      List <Hospital> hospitals = hospitalRepository.findAll();
      return hospitals;
    }

}
