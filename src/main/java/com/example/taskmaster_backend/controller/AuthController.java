package com.example.taskmaster_backend.controller;

import com.example.taskmaster_backend.dto.LoginRequestDTO;
import com.example.taskmaster_backend.dto.RegisterRequestDTO;
import com.example.taskmaster_backend.entity.User;
import com.example.taskmaster_backend.service.JwtService;
import com.example.taskmaster_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwt;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO dto) {

        if (userService.findByUsername(dto.getUsername()).isPresent())
            return ResponseEntity.badRequest().body("Username already exists");

        if (userService.findByEmail(dto.getEmail()).isPresent())
            return ResponseEntity.badRequest().body("Email already exists");

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        User saved = userService.createUser(user);

        UserDetails userDetails = userService.loadUserByUsername(saved.getUsername());
        String token = jwt.generateToken(userDetails);

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("user", saved);

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(), dto.getPassword()
                )
        );

        UserDetails ud = userService.loadUserByUsername(dto.getUsername());
        User user = userService.findByUsername(dto.getUsername()).orElseThrow();

        String token = jwt.generateToken(ud);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", user
        ));
    }

}
