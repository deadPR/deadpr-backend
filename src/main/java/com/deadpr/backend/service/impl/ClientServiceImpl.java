package com.deadpr.backend.service.impl;

import com.deadpr.backend.dto.client.CreateBookingRequestDto;
import com.deadpr.backend.dto.client.UpdateProfileRequestDto;
import com.deadpr.backend.model.Booking;
import com.deadpr.backend.model.BookingStatus;
import com.deadpr.backend.model.Role;
import com.deadpr.backend.model.TrainingPackage;
import com.deadpr.backend.model.User;
import com.deadpr.backend.repository.BookingRepository;
import com.deadpr.backend.repository.TrainingPackageRepository;
import com.deadpr.backend.repository.UserRepository;
import com.deadpr.backend.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingPackageRepository trainingPackageRepository;

    @Override
    public Booking bookPackage(CreateBookingRequestDto request, String clientEmail) {
        log.info("Client with email {} is attempting to book package {}", clientEmail, request.getPackageId());

        User client = userRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found with email: " + clientEmail));

        if (client.getRole() != Role.ROLE_CLIENT) {
            log.warn("SECURITY ALERT: Non-client user {} with role {} tried to book a package.", clientEmail, client.getRole());
            throw new AccessDeniedException("Only clients can book packages.");
        }

        TrainingPackage trainingPackage = trainingPackageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found with ID: " + request.getPackageId()));
        boolean alreadyBooked = bookingRepository.existsByClientAndTrainingPackageAndStatus(client, trainingPackage, BookingStatus.ACTIVE);
        if (alreadyBooked) {
            log.warn("Client {} tried to book package {} which is already active.", clientEmail, request.getPackageId());
            throw new IllegalStateException("You already have an active booking for this package.");
        }

        Booking booking = new Booking();
        booking.setClient(client);
        booking.setTrainingPackage(trainingPackage);
        booking.setTrainer(trainingPackage.getTrainer());
        booking.setBookingDate(LocalDate.now());
        booking.setExpiryDate(LocalDate.now().plusDays(trainingPackage.getDurationInDays()));
        booking.setStatus(BookingStatus.ACTIVE);

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created successfully with ID {} for client {}", savedBooking.getId(), clientEmail);

        return savedBooking;
    }

    @Override
    public List<Booking> getMyBookings(String clientEmail) {
        log.info("Fetching bookings for client with email: {}", clientEmail);

        User client = userRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found with email: " + clientEmail));

        return bookingRepository.findByClient(client);
    }
    @Override
    public User updateProfile(String userEmail, UpdateProfileRequestDto request) {
        log.info("User {} is attempting to update their profile", userEmail);

        User userToUpdate = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        userToUpdate.setName(request.getName());
        userToUpdate.setPhoneNumber(request.getPhoneNumber());
        userToUpdate.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(userToUpdate);
        log.info("User {} profile updated successfully", userEmail);

        return updatedUser;
    }
}

