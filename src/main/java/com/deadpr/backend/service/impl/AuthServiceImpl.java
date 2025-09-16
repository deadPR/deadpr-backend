package com.deadpr.backend.service.impl;

import com.deadpr.backend.dto.auth.RegisterRequestDto;
import com.deadpr.backend.model.Role;
import com.deadpr.backend.model.User;
import com.deadpr.backend.repository.UserRepository;
import com.deadpr.backend.service.AuthService;
import com.deadpr.backend.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import com.deadpr.backend.dto.auth.LoginRequestDto;
import com.deadpr.backend.dto.auth.LoginResponseDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;


    @Override
    public User registerUser(RegisterRequestDto registerRequest) {
        log.info("Attempting to register new user with email: {}", registerRequest.getEmail());

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("Registration failed: Email already in use - {}", registerRequest.getEmail());
            throw new IllegalStateException("Email already in use.");
        }

        User newUser = new User();
        newUser.setName(registerRequest.getName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPhoneNumber(registerRequest.getPhoneNumber());

        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        newUser.setRole(Role.ROLE_CLIENT);
        newUser.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return savedUser;
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequest) {
        log.info("Attempting login for email: {}", loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            log.info("Login successful for email: {}", loginRequest.getEmail());
            return new LoginResponseDto(token);
        } else {
            log.warn("Login failed: Invalid credentials for email: {}", loginRequest.getEmail());
            throw new RuntimeException("Invalid credentials");

        }
    }
}