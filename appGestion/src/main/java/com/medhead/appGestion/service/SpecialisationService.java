package com.medhead.appGestion.service;

import com.medhead.appGestion.model.Specialisation;
import com.medhead.appGestion.repository.SpecialisationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialisationService {
    @Autowired
    private SpecialisationRepository specialisationRepository;

    @Transactional
    public Specialisation addSpecialisation(Specialisation specialisation) {
        return specialisationRepository.save(specialisation);
    }

    @Transactional
    public ResponseEntity<String> deleteSpecialisationById(Long id) {
        if(specialisationRepository.existsById(id)) {
            specialisationRepository.deleteById(id);
            return ResponseEntity.ok("Suppression réussie");
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Suppression invalides");
        }
    }

    @Transactional
    public List<Specialisation> getAllSpecialisation(){

        List <Specialisation> Specialisations = specialisationRepository.findAll();

        return Specialisations;
    }

    @Transactional
    public Specialisation getSpecialisationByName(String name){

        Specialisation specialisation = specialisationRepository.getSpecialisationByName(name);

        return specialisation;
    }
}
