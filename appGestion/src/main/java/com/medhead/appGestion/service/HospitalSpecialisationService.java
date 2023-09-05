package com.medhead.appGestion.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medhead.appGestion.model.Hospital;
import com.medhead.appGestion.model.HospitalSpecialisation;
import com.medhead.appGestion.model.Specialisation;
import com.medhead.appGestion.repository.HospitalRepository;
import com.medhead.appGestion.repository.HospitalSpecialisationRepository;
import com.medhead.appGestion.repository.SpecialisationRepository;
import jakarta.transaction.Transactional;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

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

    public  JSONArray getAllInformationsBySpecialisation(String specialisation, String coordinates){

        List<HospitalSpecialisation> hospitalSpecialisations = hospitalSpecialisationRepository.findBySpecialisationName(specialisation);
        JSONArray arr = new JSONArray();
        for (HospitalSpecialisation spe : hospitalSpecialisations) {
            JSONObject jo = new JSONObject();
            jo.put("Hospital",spe.getHospital().getName());
            jo.put("available",spe.getAvailableLocation());
            jo.put("distance",distance(spe.getHospital().getCoordinates(),coordinates));
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
    @Transactional
    public void  addHospitalSpecialisation(){
        JSONArray arr = new JSONArray();
        JSONObject jo = new JSONObject();

        Optional<Hospital> hospital = hospitalRepository.findById(2L);
        Optional<Specialisation> specialisation = specialisationRepository.findById(104L);


        HospitalSpecialisation special = new HospitalSpecialisation();
        special.setHospital(hospital.get());
        special.setSpecialisation(specialisation.get());
        hospitalSpecialisationRepository.save(special);
        jo.put("message","good ajout!");
        arr.add(jo);

    }
    public JSONObject getCord(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String apiUrl = "https://nominatim.openstreetmap.org/search?q=" + encodedAddress + "&format=json";

            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiUrl);


            HttpResponse response = httpClient.execute(httpGet);
            String responseString = EntityUtils.toString(response.getEntity());

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(responseString);

            if (obj instanceof JSONArray) {
                JSONArray result = (JSONArray) obj;
                String jsonString = result.get(0).toString();

                JSONParser parserResult = new JSONParser();
                JSONObject jsonObject = (JSONObject) parserResult.parse(jsonString);
                String lat = jsonObject.get("lat").toString();
                String lon = jsonObject.get("lon").toString();

                JSONObject jsonResult = new JSONObject();
                jsonResult.put("latitude", lat);
                jsonResult.put("longitude", lon);

                return jsonResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("error", "Erreur lors de la récupération des coordonnées GPS.");
        return jsonResult;

    }
    @Transactional
    public String  distance(String location, String target){
        try {

            String urlPath = location + ";" + target;
            String apiUrl = "https://router.project-osrm.org/route/v1/driving/" +urlPath + "?overview=false";
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiUrl);

            HttpResponse response = httpClient.execute(httpGet);
            String responseString = EntityUtils.toString(response.getEntity());
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(responseString);


            if (obj instanceof JSONObject) {
                JSONObject result = (JSONObject) obj;
                String jsonString = result.get("routes").toString();
                return extractDistance(jsonString)+"km";
            }
            return obj.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("error", "Erreur lors du calcule de distance.");
        return jsonResult.toString();
    }
    @Transactional
    public double extractDistance(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            double distance = jsonNode.get(0).get("distance").asDouble() * 0.001;
            return  Math.round(distance * 100.0) / 100.0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0;
        }
    }



    }






