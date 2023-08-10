package com.medhead.appGestion.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SpecialisationTest {
    @Test
    void testSpecialisationProperties() {
        Integer id = 1;
        String name = "Test Specialisation";

        Specialisation specialisation = new Specialisation();
        specialisation.setId(id);
        specialisation.setName(name);

        assertEquals(id, specialisation.getId());
        assertEquals(name, specialisation.getName());
    }

    @Test
    void testSpecialisationHospitalSpecialisations() {
        Specialisation specialisation = new Specialisation();
        HospitalSpecialisation specialisation1 = new HospitalSpecialisation();
        HospitalSpecialisation specialisation2 = new HospitalSpecialisation();
        Specialisation special1 = new Specialisation();
        Specialisation special2 = new Specialisation();
        special1.setName("Specialisation1");
        special2.setName("Specialisation2");
        specialisation1.setSpecialisation(specialisation);
        specialisation2.setSpecialisation(specialisation);
        Set<HospitalSpecialisation> set = new HashSet<HospitalSpecialisation>();
        set.add(specialisation1);
        set.add(specialisation2);
        specialisation.setHospitalSpecialisations(set);
        assertEquals(2, specialisation.getHospitalSpecialisations().size());
        assertEquals(specialisation, specialisation1.getSpecialisation());
        assertEquals(specialisation, specialisation2.getSpecialisation());
    }
}
