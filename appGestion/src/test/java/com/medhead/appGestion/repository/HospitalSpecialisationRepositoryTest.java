package com.medhead.appGestion.repository;

import com.medhead.appGestion.model.HospitalSpecialisation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureMockMvc
public class HospitalSpecialisationRepositoryTest {
    @Autowired
    public MockMvc mockMvc;
    @Autowired
    HospitalSpecialisationRepository hospitalSpecialisationRepository;

    @Test
    public void checkIfLinkIsAlreadyTrue(){
        Long hospitalId =  1l;
        Long hospitalSpecialisationId = 1l;
        HospitalSpecialisation hospitalSpecialisation = hospitalSpecialisationRepository.checkIfLinkIsAlready(hospitalId,hospitalSpecialisationId);
        assertNotNull(hospitalSpecialisation);
    }

    @Test
    public void checkIfLinkIsAlreadyFalse(){
        Long hospitalId =  999l;
        Long hospitalSpecialisationId = 1l;
        HospitalSpecialisation hospitalSpecialisation = hospitalSpecialisationRepository.checkIfLinkIsAlready(hospitalId,hospitalSpecialisationId);
        assertNull(hospitalSpecialisation);
    }

}
