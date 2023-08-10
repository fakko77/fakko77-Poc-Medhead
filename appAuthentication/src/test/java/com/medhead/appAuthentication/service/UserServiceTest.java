package com.medhead.appAuthentication.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @Test
    public void loadUserByUsernameTestUserExist(){
        String user = "adm_morgan";
        UserDetails userObject = userDetailsService.loadUserByUsername(user);
        assertEquals(user,userObject.getUsername());

    }

    @Test
    public void loadUserByUsernameTestUserNotExist() throws Exception{
        String user = "adm_morgans";
        Boolean error = true;
        try {
            UserDetails userObject = userDetailsService.loadUserByUsername(user);
            System.out.println("userObject");
            System.out.println(userObject);
            assertNull(userObject);
        }catch(Exception e) {
            assertTrue(error);
        }
    }


}
