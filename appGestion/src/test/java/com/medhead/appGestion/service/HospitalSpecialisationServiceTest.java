package com.medhead.appGestion.service;

import com.medhead.appGestion.model.Hospital;
import com.medhead.appGestion.model.HospitalSpecialisation;
import com.medhead.appGestion.repository.HospitalSpecialisationRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HospitalSpecialisationServiceTest {
    @Autowired
    public MockMvc mockMvc;



    @Mock
    private HospitalSpecialisationRepository hospitalSpecialisationRepository;

    @InjectMocks
    private HospitalSpecialisationService hospitalSpecialisationService;

    @Test
    public void getAllHospitalSpecialisationTest(){
        List<HospitalSpecialisation> list = hospitalSpecialisationService.getAllHospitalSpecialisation();
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCancelBookHospital() {
        Long hospitalSpecialisationId = 1L;

        HospitalSpecialisation specialisationA = new HospitalSpecialisation();
        specialisationA.setId(Math.toIntExact(hospitalSpecialisationId));
        specialisationA.setAvailableLocation(5L); // Initial available location

        when(hospitalSpecialisationRepository.getById(hospitalSpecialisationId)).thenReturn(specialisationA);

        JSONArray result = hospitalSpecialisationService.CancelBookHospital(hospitalSpecialisationId);

        verify(hospitalSpecialisationRepository, times(1)).getById(hospitalSpecialisationId);

        JSONObject expectedObject = new JSONObject();
        expectedObject.put("message", "Cancel book good");

        JSONArray expectedArray = new JSONArray();
        JSONObject expectedObject1 = new JSONObject();

        expectedObject1.put("message", "Cancel book good");
        expectedArray.add(expectedObject1);


        assertEquals(expectedArray.toString(), result.toString());

        // Check if availableLocation is updated
        assertEquals(Optional.of(6L), Optional.of(specialisationA.getAvailableLocation()));
    }



    @Test
    public void testBookHospitalWithAvailableLocation() {
        Long hospitalSpecialisationId = 1L;

        HospitalSpecialisation specialisationA = new HospitalSpecialisation();
        specialisationA.setId(Math.toIntExact(hospitalSpecialisationId));
        specialisationA.setAvailableLocation(5L); // Initial available location

        when(hospitalSpecialisationRepository.getById(hospitalSpecialisationId)).thenReturn(specialisationA);

        JSONArray result = hospitalSpecialisationService.bookHospital(hospitalSpecialisationId);

        verify(hospitalSpecialisationRepository, times(1)).getById(hospitalSpecialisationId);

        JSONObject expectedObject = new JSONObject();
        expectedObject.put("message", "Good booK");

        JSONArray expectedArray = new JSONArray();
        JSONObject expectedObject1 = new JSONObject();

        expectedObject1.put("message","Good booK");
        expectedArray.add(expectedObject1);


        assertEquals(expectedArray.toString(), result.toString());

        // Check if availableLocation is updated
        assertEquals( (Optional.of(4L))  , Optional.of(specialisationA.getAvailableLocation()));
    }

    @Test
    public void testBookHospitalWithNoAvailableLocation() {
        Long hospitalSpecialisationId = 1L;

        HospitalSpecialisation specialisationA = new HospitalSpecialisation();
        specialisationA.setId(Math.toIntExact(hospitalSpecialisationId));
        specialisationA.setAvailableLocation(0L); // No available location

        when(hospitalSpecialisationRepository.getById(hospitalSpecialisationId)).thenReturn(specialisationA);

        JSONArray result = hospitalSpecialisationService.bookHospital(hospitalSpecialisationId);

        verify(hospitalSpecialisationRepository, times(1)).getById(hospitalSpecialisationId);

        JSONObject expectedObject = new JSONObject();
        expectedObject.put("message", "ERROR - No available location");

        JSONArray expectedArray = new JSONArray();
        JSONObject expectedObject1 = new JSONObject();

        expectedObject1.put("message", "ERROR - No available location");
        expectedArray.add(expectedObject1);

        assertEquals(expectedArray.toString(), result.toString());

        // Check if availableLocation is not updated
        assertEquals(Optional.of(0L), Optional.of(specialisationA.getAvailableLocation()));
    }



    @Test
    public void testGetAllInformationsBySpecialisation() {
        String specialisation = "Cardiology";

        Hospital hospitalA = new Hospital();
        hospitalA.setName("Hospital A");

        Hospital hospitalB = new Hospital();
        hospitalB.setName("Hospital B");

        HospitalSpecialisation specialisationA = new HospitalSpecialisation();
        specialisationA.setId(1);
        specialisationA.setHospital(hospitalA);
        specialisationA.setAvailableLocation(5L);

        HospitalSpecialisation specialisationB = new HospitalSpecialisation();
        specialisationB.setId(2);
        specialisationB.setHospital(hospitalB);
        specialisationB.setAvailableLocation(3L);

        List<HospitalSpecialisation> mockSpecialisations = new ArrayList<>();
        mockSpecialisations.add(specialisationA);
        mockSpecialisations.add(specialisationB);

        when(hospitalSpecialisationRepository.findBySpecialisationName(specialisation)).thenReturn(mockSpecialisations);

        JSONArray result = hospitalSpecialisationService.getAllInformationsBySpecialisation(specialisation);

        verify(hospitalSpecialisationRepository, times(1)).findBySpecialisationName(specialisation);

        JSONArray expectedArray = new JSONArray();

        JSONObject expectedObjectA = new JSONObject();
        expectedObjectA.put("Hospital", "Hospital A");
        expectedObjectA.put("available", 5L);
        expectedObjectA.put("id", 1L);
        expectedArray.add(expectedObjectA);

        JSONObject expectedObjectB = new JSONObject();
        expectedObjectB.put("Hospital", "Hospital B");
        expectedObjectB.put("available", 3L);
        expectedObjectB.put("id", 2L);
        expectedArray.add(expectedObjectB);

        assertEquals(expectedArray.toString(), result.toString());
    }
}
