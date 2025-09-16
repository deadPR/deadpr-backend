package com.deadpr.backend.dto.admin;

import lombok.Data;

@Data
public class CreatePackageRequestDto {
    private String name;
    private String description;
    private double price;
    private int durationInDays;
    private String trainerId;
}