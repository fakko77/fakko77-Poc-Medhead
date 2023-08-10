package com.medhead.appGestion.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HospitalSpecialisationTest {
    @Test
    void testHospitalSpecialisationProperties() {
        Integer id = 1;
        Long availableLocation = 10L;

        HospitalSpecialisation hospitalSpecialisation = new HospitalSpecialisation();
        hospitalSpecialisation.setId(id);
        hospitalSpecialisation.setAvailableLocation(availableLocation);

        assertEquals(id, hospitalSpecialisation.getId());
        assertEquals(availableLocation, hospitalSpecialisation.getAvailableLocation());
    }

    @Test
    void testHospitalSpecialisationHospital() {
        HospitalSpecialisation hospitalSpecialisation = new HospitalSpecialisation();
        Hospital hospital = new Hospital();
        hospitalSpecialisation.setHospital(hospital);

        assertEquals(hospital, hospitalSpecialisation.getHospital());
    }

    @Test
    void testHospitalSpecialisationSpecialisation() {
        HospitalSpecialisation hospitalSpecialisation = new HospitalSpecialisation();
        Specialisation specialisation = new Specialisation();
        hospitalSpecialisation.setSpecialisation(specialisation);

        assertEquals(specialisation, hospitalSpecialisation.getSpecialisation());
    }
}
