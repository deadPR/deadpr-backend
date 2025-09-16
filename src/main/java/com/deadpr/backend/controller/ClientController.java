package com.deadpr.backend.controller;

import com.deadpr.backend.dto.client.CreateBookingRequestDto;
import com.deadpr.backend.dto.client.UpdateProfileRequestDto;
import com.deadpr.backend.model.Booking;
import com.deadpr.backend.model.User;
import com.deadpr.backend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/bookings")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Booking> bookPackage(@RequestBody CreateBookingRequestDto request, Authentication authentication) {
        String clientEmail = authentication.getName();
        Booking newBooking = clientService.bookPackage(request, clientEmail);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<Booking>> getMyBookings(Authentication authentication) {
        String clientEmail = authentication.getName();
        List<Booking> bookings = clientService.getMyBookings(clientEmail);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('CLIENT', 'TRAINER', 'ADMIN')")
    public ResponseEntity<User> updateProfile(@RequestBody UpdateProfileRequestDto request, Authentication authentication) {
        String userEmail = authentication.getName();
        User updatedUser = clientService.updateProfile(userEmail, request);
        return ResponseEntity.ok(updatedUser);
    }
}