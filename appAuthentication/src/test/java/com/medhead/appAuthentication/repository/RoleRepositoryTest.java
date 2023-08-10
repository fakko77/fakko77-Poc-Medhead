package com.medhead.appAuthentication.repository;

import com.medhead.appAuthentication.model.ERole;
import com.medhead.appAuthentication.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.medhead.appAuthentication.model.ERole.ROLE_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class RoleRepositoryTest {
    @Autowired
     MockMvc mockMvc;

    @MockBean
    RoleRepository roleRepository;


    @Test
    public void findByNameTestRoleUser(){
        ERole role = ERole.ROLE_USER;
        Optional roleTest = roleRepository.findByName(role);
        assertEquals(roleTest.of(role).get(), role);
    }

    @Test
    public void findByNameTestRoleAdmin(){
        ERole role = ERole.ROLE_ADMIN;
        Optional roleTest = roleRepository.findByName(role);
        assertEquals(roleTest.of(role).get(), role);
    }


    @Test
    public void findByNameTestNotRoleUser(){
        ERole role = ERole.ROLE_USER;
        ERole roleAdmin = ERole.ROLE_ADMIN;
        Optional roleTest = roleRepository.findByName(role);
        assertFalse(roleTest.of(role).get().equals( roleAdmin));
    }

    @Test
    public void findByNameTestNotRoleAdmin(){
        ERole role = ERole.ROLE_USER;
        ERole roleAdmin = ERole.ROLE_ADMIN;
        Optional roleTest = roleRepository.findByName(roleAdmin);
        assertFalse(roleTest.of(roleAdmin).get().equals(role));
    }


    @Test
    public void testFindByName_RoleNotFound() {

        ERole roleName = ERole.ROLE_USER;
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        assertFalse(roleOptional.isPresent());
    }


}
