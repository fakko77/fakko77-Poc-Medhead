package com.medhead.appGestion.service;

import com.medhead.appGestion.model.Hospital;
import com.medhead.appGestion.repository.HospitalRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HospitalServiceTest {
    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private HospitalService hospitalService;

    @Test
    void testAddHospital() {
        Hospital hospitalToAdd = new Hospital();
        hospitalToAdd.setId(1);
        hospitalToAdd.setName("Hospital A");

        // Mock the behavior of hospitalRepository.save()
        when(hospitalRepository.save(hospitalToAdd)).thenReturn(hospitalToAdd);

        Hospital addedHospital = hospitalService.addHospital(hospitalToAdd);

        // Verify that the hospitalRepository.save() method was called once with the correct Hospital object
        verify(hospitalRepository, times(1)).save(hospitalToAdd);

        // Check the returned Hospital object
        assertEquals(hospitalToAdd, addedHospital);
    }

    @Test
    void testDeleteHospitalById_ExistingHospital() {
        Long idToDelete = 1L;

        // Mock the behavior of hospitalRepository.existsById()
        when(hospitalRepository.existsById(idToDelete)).thenReturn(true);

        ResponseEntity<String> response = hospitalService.deleteHospitalById(idToDelete);

        // Verify that the hospitalRepository.existsById() method was called once with the correct ID
        verify(hospitalRepository, times(1)).existsById(idToDelete);

        // Verify that the hospitalRepository.deleteById() method was called once with the correct ID
        verify(hospitalRepository, times(1)).deleteById(idToDelete);

        // Check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Suppression réussie", response.getBody());
    }

    @Test
    void testDeleteHospitalById_NonExistingHospital() {
        Long idToDelete = 1L;

        // Mock the behavior of hospitalRepository.existsById()
        when(hospitalRepository.existsById(idToDelete)).thenReturn(false);

        ResponseEntity<String> response = hospitalService.deleteHospitalById(idToDelete);

        // Verify that the hospitalRepository.existsById() method was called once with the correct ID
        verify(hospitalRepository, times(1)).existsById(idToDelete);

        // Verify that the hospitalRepository.deleteById() method was not called
        verify(hospitalRepository, never()).deleteById(anyLong());

        // Check the response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Suppression invalides", response.getBody());
    }

    @Test
    void testGetAllHospital() {
        // Create some sample Hospital objects for testing
        Hospital hospital1 = new Hospital();
        hospital1.setId(1);
        hospital1.setName("Hospital A");

        Hospital hospital2 = new Hospital();
        hospital2.setId(2);
        hospital2.setName("Hospital B");

        List<Hospital> expectedHospitals = new ArrayList<>();
        expectedHospitals.add(hospital1);
        expectedHospitals.add(hospital2);

        // Mock the behavior of hospitalRepository.findAll()
        when(hospitalRepository.findAll()).thenReturn(expectedHospitals);

        List<Hospital> actualHospitals = hospitalService.getAllHospital();

        // Verify that the hospitalRepository.findAll() method was called once
        verify(hospitalRepository, times(1)).findAll();

        // Check the returned list of Hospital objects
        assertEquals(expectedHospitals, actualHospitals);
    }


    /*
    @Test
    public void addHospitalTest(){
        Hospital hospitalTest = new Hospital();
        hospitalTest.setName("Hospital Test");
        hospitalService.addHospital(hospitalTest);

        Hospital hospital = hospitalRepository.findHospitalByName("Hospital Test");
        assertNotNull(hospital);
        hospitalRepository.delete(hospital);
    }

    @Test
    public void getAllHospitalTest(){
        List<Hospital> list = hospitalService.getAllHospital();
        assertFalse(list.isEmpty());
    }

*/
}
