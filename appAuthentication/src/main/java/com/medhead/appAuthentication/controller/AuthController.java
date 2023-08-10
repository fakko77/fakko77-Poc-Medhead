package com.medhead.appAuthentication.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.medhead.appAuthentication.jwt.JwtUtils;
import com.medhead.appAuthentication.model.ERole;
import com.medhead.appAuthentication.model.Role;
import com.medhead.appAuthentication.model.User;
import com.medhead.appAuthentication.repository.RoleRepository;
import com.medhead.appAuthentication.repository.UserRepository;
import com.medhead.appAuthentication.service.UserDetailsImpl;
import com.medhead.appAuthentication.util.request.LoginRequest;
import com.medhead.appAuthentication.util.request.SignupRequest;
import com.medhead.appAuthentication.util.response.JwtResponse;
import com.medhead.appAuthentication.util.response.MessageResponse;
import jakarta.validation.Valid;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/app")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }


    @PostMapping("/adduser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        System.out.println(strRoles.stream().iterator().next().getClass());
        System.out.println(roleRepository.findAll());
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }  else  {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;

                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody public JSONObject  adminAccess() {
        Map<String, String> map = new HashMap<>();
        map.put("response", "good access");
        JSONObject jo = new JSONObject(map);
        return jo;

    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public JSONObject userAccess() {
        Map<String, String> map = new HashMap<>();
        map.put("response", "User content.");
        JSONObject jo = new JSONObject(map);
        return jo;
    }

    @GetMapping("/all")
    public JSONObject allAccess() {
        Map<String, String> map = new HashMap<>();
        map.put("response", "Public Content.");
        JSONObject jo = new JSONObject(map);
        return jo;

    }

    @DeleteMapping(value = "/delete/{name}")
    public  ResponseEntity<?> deletePost(@PathVariable String name) {

        try {
            User user = userRepository.getByUsername(name);
            userRepository.delete(user);
            return ResponseEntity.ok(new MessageResponse("User delete successfully!"));
        }catch(Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: doesn't exist!"));
        }

    }

}
