package com.medhead.appGestion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.medhead.appGestion.model.Hospital;
import com.medhead.appGestion.model.Specialisation;
import com.medhead.appGestion.repository.HospitalRepository;
import com.medhead.appGestion.repository.SpecialisationRepository;
import com.medhead.appGestion.service.HospitalSpecialisationService;
import com.medhead.appGestion.service.SpecialisationService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceControllerTest {
    @Autowired
    public MockMvc mockMvc;
    @Autowired
    public HospitalRepository hospitalRepository;

    @Autowired
    public SpecialisationRepository specialisationRepository;

    @Mock
    private SpecialisationService specialisationService;

    @MockBean
    private HospitalSpecialisationService hospitalSpecialisationService;

    @Test
    @WithMockUser(roles = {"USER"})
    public void getAllHospitalTestAccess() throws Exception {
        mockMvc.perform(get("/allHospitals"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = {"USER"})
    public void getAllSpecialisationTestAccess() throws Exception {
        mockMvc.perform(get("/allSpecialisations"))
                .andExpect(status().isOk());
    }

    @Test
    public void findBySpecialisationNameTestNotExist() throws Exception {
        mockMvc.perform(get("/Specialisation/TestSpecialisation"))
                .andExpect(status().is(401));
    }

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    @WithMockUser(roles = {"USER"})
    public void testInsertHospital() throws Exception {
        String url = "/addHospital";
        Hospital hospitalTest = new Hospital();
        hospitalTest.setId(999);
        hospitalTest.setName("HospitalTest");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(hospitalTest );

        mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());
        Hospital hospital = hospitalRepository.findHospitalByName("HospitalTest");
        hospitalRepository.delete(hospital);

    }
    @Test
    @WithMockUser(roles = {"USER"})
    public void testInsertSpecialisation() throws Exception {
        String url = "/addSpecialisation";
        Specialisation specialisationTest = new Specialisation();
        specialisationTest.setId(999);
        specialisationTest.setName("SpecialisationTest");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(specialisationTest );

        mockMvc.perform(post(url).contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());
        Specialisation specialisation = specialisationRepository.getSpecialisationByName("SpecialisationTest");
        specialisationRepository.delete(specialisation);
    }


    @Test
    @WithMockUser(roles = "USER")
    public void testHospitalBookWithUserRole() throws Exception {
        Long hospitalSpecialisationId = 1L;

        JSONArray expectedResult = new JSONArray();
        JSONObject expectedObject = new JSONObject();
        expectedObject.put("message", "Good booK");
        expectedResult.add(expectedObject);

        when(hospitalSpecialisationService.bookHospital(hospitalSpecialisationId)).thenReturn(expectedResult);

        mockMvc.perform(get("/book/{id}", hospitalSpecialisationId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult.toString()));

        verify(hospitalSpecialisationService, times(1)).bookHospital(hospitalSpecialisationId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testHospitalBookWithAdminRole() throws Exception {
        Long hospitalSpecialisationId = 1L;

        JSONArray expectedResult = new JSONArray();
        JSONObject expectedObject = new JSONObject();
        expectedObject.put("message", "Good booK");
        expectedResult.add(expectedObject);

        when(hospitalSpecialisationService.bookHospital(hospitalSpecialisationId)).thenReturn(expectedResult);

        mockMvc.perform(get("/book/{id}", hospitalSpecialisationId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult.toString()));

        verify(hospitalSpecialisationService, times(1)).bookHospital(hospitalSpecialisationId);
    }

    @Test
    public void testHospitalBookWithoutUser() throws Exception {
        Long hospitalSpecialisationId = 1L;

        mockMvc.perform(get("/book/{id}", hospitalSpecialisationId))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(hospitalSpecialisationService);
    }


    @Test
    @WithMockUser(roles = "USER")
    public void testHospitalBookCancelWithUserRole() throws Exception {
        Long hospitalSpecialisationId = 1L;

        JSONArray expectedResult = new JSONArray();
        JSONObject expectedObject = new JSONObject();
        expectedObject.put("message", "Cancel book good");
        expectedResult.add(expectedObject);

        when(hospitalSpecialisationService.CancelBookHospital(hospitalSpecialisationId)).thenReturn(expectedResult);

        mockMvc.perform(get("/CancelBook/{id}", hospitalSpecialisationId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult.toString()));

        verify(hospitalSpecialisationService, times(1)).CancelBookHospital(hospitalSpecialisationId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testHospitalBookCancelWithAdminRole() throws Exception {
        Long hospitalSpecialisationId = 1L;

        JSONArray expectedResult = new JSONArray();
        JSONObject expectedObject = new JSONObject();
        expectedObject.put("message", "Cancel book good");
        expectedResult.add(expectedObject);

        when(hospitalSpecialisationService.CancelBookHospital(hospitalSpecialisationId)).thenReturn(expectedResult);

        mockMvc.perform(get("/CancelBook/{id}", hospitalSpecialisationId))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult.toString()));

        verify(hospitalSpecialisationService, times(1)).CancelBookHospital(hospitalSpecialisationId);
    }

    @Test
    public void testHospitalBookCancelWithoutUser() throws Exception {
        Long hospitalSpecialisationId = 1L;

        mockMvc.perform(get("/CancelBook/{id}", hospitalSpecialisationId))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(hospitalSpecialisationService);
    }
    @Test
    @WithMockUser(roles = "USER")
    public void testFindBySpecialisationName() throws Exception {
        String specialisationName = "Immunologie";
        Specialisation mockSpecialisation = new Specialisation();
        mockSpecialisation.setName(specialisationName);

        when(specialisationService.getSpecialisationByName(specialisationName)).thenReturn(mockSpecialisation);

        mockMvc.perform(get("/Specialisation/"+specialisationName, specialisationName))
                .andExpect(status().isOk());

    }

    @Test
    public void testFindBySpecialisationNameNotFound() throws Exception {
        String specialisationName = "InvalidSpecialisation";

        when(specialisationService.getSpecialisationByName(specialisationName)).thenReturn(null);

        mockMvc.perform(get("/Specialisation/{name}", specialisationName))
                .andExpect(status().is(401));
    }

    @Test
    void testGetLocation() throws Exception {

        String address = "123 Main Street";
        JSONObject expectedJsonObject = new JSONObject();
        expectedJsonObject.put("latitude", "51.5074");
        expectedJsonObject.put("longitude", "0.1278");

        when(hospitalSpecialisationService.getCord(address)).thenReturn(expectedJsonObject);

        mockMvc.perform(MockMvcRequestBuilders.get("/getlocation/{address}", address))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.latitude").value("51.5074"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.longitude").value("0.1278"));
    }


}
