package com.medhead.appGestion.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class HospitalRepositoryTest {
    @Autowired
    public MockMvc mockMvc;
    @Autowired
    HospitalRepository hospitalRepository;

    @Test
    public void existsByNameTestTrue(){
    String name = "Hôpital Fred Brooks";
    Boolean hospital = hospitalRepository. existsByName(name);
    assertTrue(hospital);
    }

    @Test
    public void existsByNameTestFalse(){
        String name = "Hôpital Fred Brookss";
        Boolean hospital = hospitalRepository. existsByName(name);
        assertFalse(hospital);
    }

}
