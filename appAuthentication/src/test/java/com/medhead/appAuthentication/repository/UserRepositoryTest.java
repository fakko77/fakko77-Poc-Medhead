package com.medhead.appAuthentication.repository;

import com.medhead.appAuthentication.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRepositoryTest {
    @Autowired
    public MockMvc mockMvc;
    @MockBean
    UserRepository userRepository;




    @Test
    public void testExistsByUsername_UserExists() {

        String username = "testUser";

        when(userRepository.existsByUsername(username)).thenReturn(true);
        boolean exists = userRepository.existsByUsername(username);
        assertTrue(exists);
    }

    @Test
    public void testExistsByUsername_UserDoesNotExist() {
        String username = "nonExistentUser";
        when(userRepository.existsByUsername(username)).thenReturn(false);
        boolean exists = userRepository.existsByUsername(username);
        assertFalse(exists);
    }



    @Test
    public void testExistsByEmail_EmailExists() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        boolean exists = userRepository.existsByEmail(email);
        assertTrue(exists);
    }
    @Test
    public void testExistsByEmail_EmailDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        boolean exists = userRepository.existsByEmail(email);
        assertFalse(exists);
    }


    @Test
    public void testFindByUsername_UserFound() {

        String username = "testUser";
        User mockUser = new User(username, "test@email.com","testPassword"); // Create a mock user
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        Optional<User> userOptional = userRepository.findByUsername(username);
        assertTrue(userOptional.isPresent());
        assertEquals(mockUser, userOptional.get());
    }

    @Test
    public void testFindByUsername_UserNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Optional<User> userOptional = userRepository.findByUsername(username);
        assertFalse(userOptional.isPresent());
    }
}
