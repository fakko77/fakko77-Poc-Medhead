package com.medhead.appGestion.repository;

import com.medhead.appGestion.model.Specialisation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SpecialisationRepositoryTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    SpecialisationRepository specialisationRepository;


    @Test
    public void existsByNameTestTrue(){
        String name = "Cardiologie";
        Boolean specialisation = specialisationRepository. existsByName(name);
        assertTrue(specialisation);
    }

    @Test
    public void existsByNameTestFalse(){
        String name = "Cardiologies";
        Boolean  specialisation = specialisationRepository. existsByName(name);
        assertFalse(specialisation );
    }


    @Test
    public void getSpecialisationByNameTrue(){
        String name = "Cardiologie";
        Specialisation specialisation = specialisationRepository. getSpecialisationByName(name);
        assertEquals(specialisation.getName(), name);
    }

    @Test
    public void getSpecialisationByNameFalse() throws Exception {
        String name = "Cardiologies";
        Specialisation specialisation = specialisationRepository. getSpecialisationByName(name);
        assertNull(specialisation);
    }


}
