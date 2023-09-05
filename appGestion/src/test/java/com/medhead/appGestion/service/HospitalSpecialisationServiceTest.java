package com.medhead.appGestion.service;

import com.medhead.appGestion.model.Hospital;
import com.medhead.appGestion.model.HospitalSpecialisation;
import com.medhead.appGestion.repository.HospitalSpecialisationRepository;
import io.jsonwebtoken.io.IOException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HospitalSpecialisationServiceTest {
    @Autowired
    public MockMvc mockMvc;


    @Mock
    private CloseableHttpClient httpClient;
    @Mock
    private HospitalSpecialisationRepository hospitalSpecialisationRepository;

    @InjectMocks
    private HospitalSpecialisationService hospitalSpecialisationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

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
void testGetCordSuccess() throws IOException, ParseException {
    // Mocking the HTTP response
    HttpResponse httpResponse = mock(HttpResponse.class);
    StatusLine statusLine = mock(StatusLine.class);
    when(httpResponse.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(200);

    JSONObject result = hospitalSpecialisationService.getCord("London");

    assertNotNull(result);
    assertEquals("51.5073359", result.get("latitude"));
    assertEquals("-0.12765", result.get("longitude"));
}

    @Test
    void testGetCordErrorResponse() throws IOException {

        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(500); // Simulating an error response

        JSONObject result = hospitalSpecialisationService.getCord("InvalidAddress");


        assertNotNull(result);
        assertTrue(result.containsKey("error"));
        assertEquals("Erreur lors de la récupération des coordonnées GPS.", result.get("error"));
    }



    @Test
    void testExtractDistanceSuccess() {
        String jsonResponse = "[{\"distance\": 1234.567}]";

        double result = hospitalSpecialisationService.extractDistance(jsonResponse);

        assertEquals(1.23, result, 0.01); // 1234.567 * 0.001 rounded to 2 decimal places
    }

    @Test
    void testExtractDistanceInvalidJson() {

        String invalidJsonResponse = "Invalid JSON";

        double result = hospitalSpecialisationService.extractDistance(invalidJsonResponse);

        assertEquals(-1.0, result, 0.01); // Expecting -1.0 for an invalid JSON response
    }

    @Test
    void testExtractDistanceMissingDistanceField() {

        String jsonResponseWithoutDistance = "[{\"otherField\": 1234.567}]";


        double result = hospitalSpecialisationService.extractDistance(jsonResponseWithoutDistance);


        assertEquals(-1.0, result, 0.01); // Expecting -1.0 when "distance" field is missing
    }
    @Test
    void testGetAllInformationsBySpecialisation() {
        // Mocking the behavior of hospitalSpecialisationRepository
        String specialisation = "Cardiology";
        String coordinates = "123.456,789.012";

        List<HospitalSpecialisation> mockSpecialisations = new ArrayList<>();
        HospitalSpecialisation specialisation1 = new HospitalSpecialisation();
        Hospital test1 = new Hospital();
        test1.setName("Hospital1");
        test1.setCoordinates("1.234,5.678");
        specialisation1.setHospital(test1 );
        specialisation1.setAvailableLocation(1L);
        mockSpecialisations.add(specialisation1);

        HospitalSpecialisation specialisation2 = new HospitalSpecialisation();
        Hospital test2 = new Hospital();
        test2.setName("Hospital1");
        test2.setCoordinates("1.234,5.678");
        specialisation2.setHospital(test2);
        specialisation2.setAvailableLocation(3L);
        mockSpecialisations.add(specialisation2);

        when(hospitalSpecialisationRepository.findBySpecialisationName(specialisation)).thenReturn(mockSpecialisations);


        JSONArray result = hospitalSpecialisationService.getAllInformationsBySpecialisation(specialisation, coordinates);


        assertEquals(2, result.size()); // Expecting 2 JSON objects in the array
        JSONObject jsonObject1 = (JSONObject) result.get(0);
        assertEquals("Hospital1", jsonObject1.get("Hospital"));
        assertEquals(1L, jsonObject1.get("available"));

        JSONObject jsonObject2 = (JSONObject) result.get(1);
        assertEquals("Hospital1", jsonObject2.get("Hospital"));
        assertEquals(3L, jsonObject2.get("available"));
    }
}
