package com.medhead.appAuthentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medhead.appAuthentication.jwt.JwtUtils;
import com.medhead.appAuthentication.model.ERole;
import com.medhead.appAuthentication.model.Role;
import com.medhead.appAuthentication.repository.RoleRepository;
import com.medhead.appAuthentication.repository.UserRepository;
import com.medhead.appAuthentication.util.request.LoginRequest;
import com.medhead.appAuthentication.util.request.SignupRequest;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static com.medhead.appAuthentication.model.ERole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {
    @Autowired
    public MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AuthController authenticationController;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder encoder;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }





    @Test
    @Order(10)
    public void shouldAllowAccessToUnauthenticatedUsers() throws Exception {

        mockMvc.perform(get("/app/all"))
                .andExpect(status().isOk());

    }

    @Test
    @Order(9)
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/user")).andExpect(status().is(401));
    }

    @Test
    @Order(8)
    public void shouldNotAllowAccessToUnauthenticatedAdmins() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/admin")).andExpect(status().is(401));
    }

    @Test
    @Order(7)
    @WithMockUser(roles = {"ADMIN"})
    public void shouldAllowAccessToUnauthenticatedAdmins() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/admin")).andExpect(status().isOk());
    }


    @Test
    @Order(6)
    public void testAllAccess() throws Exception {
        // Given
        Map<String, String> map = new HashMap<>();
        map.put("response", "Public Content.");
        JSONObject expectedJsonResponse = new JSONObject(map);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/app/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse.toString()));
    }

    @Test
    @Order(5)
    @WithMockUser(roles = {"USER"})
    public void testUserAccess_UserRole() throws Exception {
        mockMvc.perform(get("/app/user"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void testUserAccess_UnauthorizedRole() throws Exception {
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/app/user"))
                .andExpect(status().is(401));
    }

@Test
@Order(1)
public void testAddUser_Success() throws Exception {
    // Test data

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("newUser");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRole(new HashSet<>(Collections.singletonList("user")));

        // Mocking the UserRepository
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

        // Mocking the RoleRepository
        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        // Mocking the PasswordEncoder
        when(encoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        // Convert the SignupRequest object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(signUpRequest);

        // Perform the HTTP POST request
        MvcResult result = mockMvc.perform(post("/app/adduser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        JSONObject resultTest = new JSONObject();
        resultTest.put("message", "User registered successfully!");
        assertEquals(responseJson, resultTest.toString());

}

    @Test
    @Order(2)
    public void testAddUser_UsernameAlreadyTaken() throws Exception {
        // Test data
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("newUser");
        signUpRequest.setEmail("newuserT@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRole(new HashSet<>(Collections.singletonList("user")));

        // Mocking the UserRepository
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        // Convert the SignupRequest object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(signUpRequest);

        // Perform the HTTP POST request
        MvcResult result = mockMvc.perform(post("/app/adduser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Get and verify the response
        String responseJson = result.getResponse().getContentAsString();
        JSONObject resultTest = new JSONObject();
        resultTest.put("message","Error: Username is already taken!");
        assertEquals(responseJson, resultTest.toString());
    }

    @Test
    @Order(3)
    public void testAddUser_EmailAlreadyInUse() throws Exception {
        // Test data
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("newTUser");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRole(new HashSet<>(Collections.singletonList("user")));

        // Mocking the UserRepository
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        // Convert the SignupRequest object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(signUpRequest);

        // Perform the HTTP POST request
        MvcResult result = mockMvc.perform(post("/app/adduser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Get and verify the response
        String responseJson = result.getResponse().getContentAsString();
        JSONObject resultTest = new JSONObject();
        resultTest.put("message","Error: Email is already in use!");
        assertEquals(responseJson, resultTest.toString());

    }

    @Test
    @Order(11)
    public void delete_UserByNameSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/app/delete/newUser"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    public void delete_UserByNameError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/app/delete/newUser"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(13)
    public void testAddAdmin_Success() throws Exception {
        // Test data

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("newUser");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRole(new HashSet<>(Collections.singletonList("admin")));

        // Mocking the UserRepository
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

        // Mocking the RoleRepository
        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        // Mocking the PasswordEncoder
        when(encoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        // Convert the SignupRequest object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(signUpRequest);

        // Perform the HTTP POST request
        MvcResult result = mockMvc.perform(post("/app/adduser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        JSONObject resultTest = new JSONObject();
        resultTest.put("message", "User registered successfully!");
        assertEquals(responseJson, resultTest.toString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/app/delete/newUser"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    public void testAddNoRole_Success() throws Exception {
        // Test data

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("newUser");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRole(new HashSet<>(Collections.singletonList("")));

        // Mocking the UserRepository
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

        // Mocking the RoleRepository
        Role userRole = new Role();
        userRole.setName(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

        // Mocking the PasswordEncoder
        when(encoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        // Convert the SignupRequest object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(signUpRequest);

        // Perform the HTTP POST request
        MvcResult result = mockMvc.perform(post("/app/adduser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        JSONObject resultTest = new JSONObject();
        resultTest.put("message", "User registered successfully!");
        assertEquals(responseJson, resultTest.toString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/app/delete/newUser"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticateUser_Success() throws Exception {
        // Create a test LoginRequest object
        LoginRequest testLoginRequest = new LoginRequest();
        testLoginRequest.setUsername("basic_morgan");
        testLoginRequest.setPassword("123456");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(testLoginRequest);

        mockMvc.perform(post("/app/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

    }
    @Test
    public void testAuthenticateUser_Error() throws Exception {
        // Create a test LoginRequest object
        LoginRequest testLoginRequest = new LoginRequest();
        testLoginRequest.setUsername("basic_test");
        testLoginRequest.setPassword("123456");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(testLoginRequest);

        mockMvc.perform(post("/app/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is(401));

    }

}


