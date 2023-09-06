package com.medhead.appGestion.controller;
import com.medhead.appGestion.model.Hospital;
import com.medhead.appGestion.model.Request;
import com.medhead.appGestion.model.Specialisation;
import com.medhead.appGestion.repository.HospitalRepository;
import com.medhead.appGestion.repository.SpecialisationRepository;
import com.medhead.appGestion.service.HospitalService;
import com.medhead.appGestion.service.HospitalSpecialisationService;
import com.medhead.appGestion.service.SpecialisationService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import org.springframework.web.bind.annotation.*;
import net.minidev.json.parser.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.apache.http.client.methods.HttpPost;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class GestionController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private SpecialisationRepository specialisationRepository;
    @Autowired
    private SpecialisationService specialisationService;
    @Autowired
    private HospitalSpecialisationService hospitalSpecialisationService;



    @GetMapping("/getlocation/{address}")
    public JSONObject getLocation(@PathVariable String address) {
        return hospitalSpecialisationService.getCord(address);
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/addHospital")
    public ResponseEntity<?> AddHospital(@RequestBody Hospital hospital) {
        if (hospitalRepository.existsByName(hospital.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Hospital name already exists");
        }else {
            hospitalService.addHospital(hospital);
            return ResponseEntity.ok("Hospital is already added");
        }

    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/addSpecialisation")
    public ResponseEntity<?> AddSpecialisation(@RequestBody Specialisation specialisation) {
        if (specialisationRepository.existsByName(specialisation.getName())) {

            return ResponseEntity
                    .badRequest()
                    .body("Error: Specialisation name already exists");
        }else {
            specialisationService.addSpecialisation(specialisation);
            return ResponseEntity.ok("Specialisation is already added");
        }
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/allInfo")
    public ResponseEntity<?> hospitalAndAvailableLocation(@RequestBody Request info){
        String cord = info.getCoordinates();
        String spe = info.getSpecialisationName();
        return ResponseEntity.ok(hospitalSpecialisationService.getAllInformationsBySpecialisation(spe, cord));
    }




    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/book/{id}")
    public JSONArray hospitalBook(@PathVariable Long id){
        return hospitalSpecialisationService.bookHospital(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/CancelBook/{id}")
    public JSONArray hospitalBookCancel(@PathVariable Long id){
        return hospitalSpecialisationService.CancelBookHospital(id);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/allHospitals")
    public List<Hospital> getAllHospital(){
        return hospitalService.getAllHospital();
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/allSpecialisations")
    public List<Specialisation> getAllSpecialisation(){
        return specialisationService.getAllSpecialisation();
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/Specialisation/{name}")
    public ResponseEntity<?>  findBySpecialisationName(@PathVariable String name) {

        Specialisation specialisation =  specialisationService.getSpecialisationByName(name);
        if(specialisation == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return ResponseEntity.ok(specialisation);
        }
    }

}
