package com.deadpr.backend.dto.client;

import lombok.Data;

@Data
public class UpdateProfileRequestDto {
    private String name;
    private String phoneNumber;
}

