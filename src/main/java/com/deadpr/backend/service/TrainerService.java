package com.deadpr.backend.service;

import com.deadpr.backend.model.Booking;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TrainerService {
    List<Booking> getMyClients(String trainerEmail);
    Booking uploadPlanForClient(String trainerEmail, String bookingId, MultipartFile planFile) throws IOException;
}