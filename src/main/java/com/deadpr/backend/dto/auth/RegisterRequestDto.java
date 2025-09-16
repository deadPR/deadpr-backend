package com.deadpr.backend.dto.auth;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
}