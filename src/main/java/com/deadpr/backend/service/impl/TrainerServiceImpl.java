package com.deadpr.backend.service.impl;

import com.deadpr.backend.model.Booking;
import com.deadpr.backend.model.Trainer;
import com.deadpr.backend.model.User;
import com.deadpr.backend.repository.BookingRepository;
import com.deadpr.backend.repository.TrainerRepository;
import com.deadpr.backend.repository.UserRepository;
import com.deadpr.backend.service.FileUploadService;
import com.deadpr.backend.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private static final Logger log = LoggerFactory.getLogger(TrainerServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public List<Booking> getMyClients(String trainerEmail) {
        log.info("Fetching clients for trainer with email: {}", trainerEmail);

        User trainerUser = userRepository.findByEmail(trainerEmail)
                .orElseThrow(() -> new RuntimeException("Trainer user not found with email: " + trainerEmail));

        Trainer trainerProfile = trainerRepository.findByUserId(trainerUser.getId())
                .orElseThrow(() -> new RuntimeException("Trainer profile not found for user ID: " + trainerUser.getId()));

        List<Booking> bookings = bookingRepository.findByTrainerId(trainerProfile.getId());

        log.info("Found {} clients for trainer {}", bookings.size(), trainerEmail);
        return bookings;
    }

    @Override
    public Booking uploadPlanForClient(String trainerEmail, String bookingId, MultipartFile planFile) throws IOException {
        log.info("Trainer {} is uploading a plan for booking ID: {}", trainerEmail, bookingId);

        User trainerUser = userRepository.findByEmail(trainerEmail)
                .orElseThrow(() -> new RuntimeException("Trainer user not found with email: " + trainerEmail));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        if (!booking.getTrainer().getUser().getId().equals(trainerUser.getId())) {
            log.warn("SECURITY ALERT: Trainer {} tried to upload a plan for a booking ({}) that does not belong to them.", trainerEmail, bookingId);
            throw new AccessDeniedException("You are not authorized to upload a plan for this booking.");
        }

        String fileUrl = fileUploadService.uploadFile(planFile);
        log.info("File uploaded to Cloudinary, URL: {}", fileUrl);

        booking.setPlanDocumentUrl(fileUrl);

        Booking updatedBooking = bookingRepository.save(booking);
        log.info("Booking {} updated with plan URL.", bookingId);

        return updatedBooking;
    }
}

