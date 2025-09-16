package com.deadpr.backend.service;

import com.deadpr.backend.dto.auth.RegisterRequestDto;
import com.deadpr.backend.model.User;
import com.deadpr.backend.dto.auth.LoginRequestDto;
import com.deadpr.backend.dto.auth.LoginResponseDto;

public interface AuthService {
    User registerUser(RegisterRequestDto registerRequest);
    LoginResponseDto loginUser(LoginRequestDto loginRequest);
}