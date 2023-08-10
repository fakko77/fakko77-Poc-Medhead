package com.medhead.appGestion.service;

import com.medhead.appGestion.model.HospitalSpecialisation;
import com.medhead.appGestion.repository.HospitalRepository;
import com.medhead.appGestion.repository.HospitalSpecialisationRepository;
import com.medhead.appGestion.repository.SpecialisationRepository;
import jakarta.transaction.Transactional;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalSpecialisationService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private SpecialisationRepository specialisationRepository;
    @Autowired
    private HospitalSpecialisationRepository hospitalSpecialisationRepository;

    @Transactional
    public List<HospitalSpecialisation> getAllHospitalSpecialisation(){

        List <HospitalSpecialisation> HospitalSpecialisations = hospitalSpecialisationRepository.findAll();

        return HospitalSpecialisations;
    }


    @Transactional

    public  JSONArray getAllInformationsBySpecialisation(String specialisation){

        List<HospitalSpecialisation> hospitalSpecialisations = hospitalSpecialisationRepository.findBySpecialisationName(specialisation);
        JSONArray arr = new JSONArray();
        for (HospitalSpecialisation spe : hospitalSpecialisations) {
            JSONObject jo = new JSONObject();
            jo.put("Hospital",spe.getHospital().getName());
            jo.put("available",spe.getAvailableLocation());
            jo.put("id",spe.getId());
            arr.add(jo);
        }
       return arr;
    }

    @Transactional
    public JSONArray bookHospital(Long hospitalSpecialisationId){
        JSONArray arr = new JSONArray();
        JSONObject jo = new JSONObject();
        HospitalSpecialisation hospitalSpecialisation = hospitalSpecialisationRepository.getById(hospitalSpecialisationId);

        Long availableLocation = hospitalSpecialisation.getAvailableLocation() - 1L ;

        if (hospitalSpecialisation.getAvailableLocation() == 0){
            jo.put("message","ERROR - No available location");
        } else {
            jo.put("message","Good booK");
            hospitalSpecialisation.setAvailableLocation(availableLocation);
        }
        arr.add(jo);
        return arr;

    }


    @Transactional
    public JSONArray CancelBookHospital(Long hospitalSpecialisationId){
        JSONArray arr = new JSONArray();
        JSONObject jo = new JSONObject();
        HospitalSpecialisation hospitalSpecialisation = hospitalSpecialisationRepository.getById(hospitalSpecialisationId);

        Long availableLocation = hospitalSpecialisation.getAvailableLocation() + 1L ;

            jo.put("message","Cancel book good");
            hospitalSpecialisation.setAvailableLocation(availableLocation);


        arr.add(jo);
        return arr;

    }

}




