package com.backend.controller;

import com.backend.Entity.user;
import com.backend.dto.loginDto;
import com.backend.jwtUtils.utils;
import com.backend.repository.userRepo;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Auth")
public class AuthController {

    public AuthController(PasswordEncoder passwordEncoder, utils jwt, AuthenticationManager authenticationManager, userRepo repo) {
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
        this.authenticationManager = authenticationManager;
        this.repo = repo;
    }

    private final userRepo repo;

    private final AuthenticationManager authenticationManager;

    private final utils jwt;

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody user user) {
    var x=com.backend.Entity.user.builder()
        .username(user.getUsername())
        .password(passwordEncoder.encode(user.getPassword()))
        .build();
repo.save(x);
        return new ResponseEntity<>("Successfully registered", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody loginDto user) {
        Authentication auth= authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.username(), user.password()));

            var token=jwt.generateToken(auth.getName());
        return ResponseEntity.ok().body(token);
    }
}
