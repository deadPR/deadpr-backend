package com.deadpr.backend.controller;

import com.deadpr.backend.dto.admin.CreatePackageRequestDto;
import com.deadpr.backend.dto.admin.CreateTrainerRequestDto;
import com.deadpr.backend.model.Booking;
import com.deadpr.backend.model.TrainingPackage;
import com.deadpr.backend.model.User;
import com.deadpr.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping(value = "/trainers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createTrainer(
            @RequestPart("trainerData") CreateTrainerRequestDto request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {

        User newTrainer = adminService.createTrainer(request, profileImage);
        return new ResponseEntity<>(newTrainer, HttpStatus.CREATED);
    }

    @PostMapping("/packages")
    public ResponseEntity<TrainingPackage> createPackage(@RequestBody CreatePackageRequestDto request) {
        TrainingPackage newPackage = adminService.createPackage(request);
        return new ResponseEntity<>(newPackage, HttpStatus.CREATED);
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        Map<String, Long> stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/recent-clients")
    public ResponseEntity<List<User>> getRecentClients() {
        List<User> clients = adminService.getRecentClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/recent-bookings")
    public ResponseEntity<List<Booking>> getRecentBookings() {
        List<Booking> bookings = adminService.getRecentBookings();
        return ResponseEntity.ok(bookings);
    }
}

