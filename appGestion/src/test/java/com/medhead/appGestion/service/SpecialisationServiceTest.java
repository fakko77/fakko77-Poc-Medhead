package com.medhead.appGestion.service;

import com.medhead.appGestion.model.Specialisation;
import com.medhead.appGestion.repository.SpecialisationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SpecialisationServiceTest {
    @Autowired
    public MockMvc mockMvc;

    @Mock
    private SpecialisationRepository specialisationRepository;

    @InjectMocks
    private SpecialisationService specialisationService;
   @Test
   void testGetSpecialisationByName_ExistingSpecialisation() {
       String specialisationName = "Specialisation A";
       Specialisation expectedSpecialisation = new Specialisation();
       expectedSpecialisation.setName(specialisationName);
       when(specialisationRepository.getSpecialisationByName(specialisationName)).thenReturn(expectedSpecialisation);
       Specialisation actualSpecialisation = specialisationService.getSpecialisationByName(specialisationName);
       verify(specialisationRepository, times(1)).getSpecialisationByName(specialisationName);
       assertEquals(expectedSpecialisation, actualSpecialisation);
   }

    @Test
    void testGetSpecialisationByName_NonExistingSpecialisation() {
        String specialisationName = "Nonexistent Specialisation";
        when(specialisationRepository.getSpecialisationByName(specialisationName)).thenReturn(null);
        Specialisation actualSpecialisation = specialisationService.getSpecialisationByName(specialisationName);
        verify(specialisationRepository, times(1)).getSpecialisationByName(specialisationName);
        assertEquals(null, actualSpecialisation);
    }

    @Test
    void testDeleteSpecialisationById_SuccessfulDeletion() {
        Long idToDelete = 1L;
        when(specialisationRepository.existsById(idToDelete)).thenReturn(true);
        ResponseEntity<String> response = specialisationService.deleteSpecialisationById(idToDelete);
        verify(specialisationRepository, times(1)).existsById(idToDelete);
        verify(specialisationRepository, times(1)).deleteById(idToDelete);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Suppression réussie", response.getBody());
    }

    @Test
    void testDeleteSpecialisationById_UnsuccessfulDeletion() {
        Long idToDelete = 1L;
        when(specialisationRepository.existsById(idToDelete)).thenReturn(false);
        ResponseEntity<String> response = specialisationService.deleteSpecialisationById(idToDelete);
        verify(specialisationRepository, times(1)).existsById(idToDelete);
        verify(specialisationRepository, never()).deleteById(anyLong());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Suppression invalides", response.getBody());
    }
    @Test
    void testGetAllSpecialisation_ExistingSpecialisations() {
        Specialisation specialisation1 = new Specialisation();
        specialisation1.setId(1);
        specialisation1.setName("Specialisation A");
        Specialisation specialisation2 = new Specialisation();
        specialisation2.setId(2);
        specialisation2.setName("Specialisation B");
        List<Specialisation> expectedSpecialisations = new ArrayList<>();
        expectedSpecialisations.add(specialisation1);
        expectedSpecialisations.add(specialisation2);
        when(specialisationRepository.findAll()).thenReturn(expectedSpecialisations);
        List<Specialisation> actualSpecialisations = specialisationService.getAllSpecialisation();
        verify(specialisationRepository, times(1)).findAll();
        assertEquals(expectedSpecialisations, actualSpecialisations);
    }

    @Test
    void testGetAllSpecialisation_NoSpecialisations() {
        when(specialisationRepository.findAll()).thenReturn(new ArrayList<>());
        List<Specialisation> actualSpecialisations = specialisationService.getAllSpecialisation();
        verify(specialisationRepository, times(1)).findAll();
        assertEquals(0, actualSpecialisations.size());
    }
}
