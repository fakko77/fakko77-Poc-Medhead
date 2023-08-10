package com.medhead.appGestion.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class HospitalTest {
    @Test
    void testHospitalProperties() {
        Integer id = 1;
        String name = "Test Hospital";

        Hospital hospital = new Hospital();
        hospital.setId(id);
        hospital.setName(name);

        assertEquals(id, hospital.getId());
        assertEquals(name, hospital.getName());
    }

    @Test
    void testHospitalSpecialisations() {
        Hospital hospital = new Hospital();
        HospitalSpecialisation specialisation1 = new HospitalSpecialisation();
        HospitalSpecialisation specialisation2 = new HospitalSpecialisation();
        Specialisation special1 = new Specialisation();
        Specialisation special2 = new Specialisation();
        special1.setName("Specialisation1");
        special2.setName("Specialisation2");
        specialisation1.setSpecialisation(special1);
        specialisation2.setSpecialisation(special2);
        specialisation1.setHospital(hospital);
        specialisation2.setHospital(hospital);

        Set<HospitalSpecialisation> set = new HashSet<HospitalSpecialisation>();
        set.add(specialisation1);
        set.add(specialisation2);
        hospital.setHospitalSpecialisations(set);
        assertEquals(2, hospital.getHospitalSpecialisations().size());

        assertEquals(hospital, specialisation1.getHospital());
        assertEquals(hospital, specialisation2.getHospital());
    }
}
