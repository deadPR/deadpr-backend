package com.deadpr.backend.controller;

import com.deadpr.backend.dto.auth.LoginRequestDto;
import com.deadpr.backend.dto.auth.LoginResponseDto;
import com.deadpr.backend.dto.auth.RegisterRequestDto;
import com.deadpr.backend.model.User;
import com.deadpr.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterRequestDto registerRequest) {
        User registeredUser = authService.registerUser(registerRequest);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = authService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }
}