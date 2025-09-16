package com.deadpr.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    @DBRef
    private User client;

    @DBRef
    private Trainer trainer;

    @DBRef
    private TrainingPackage trainingPackage;

    private LocalDate bookingDate;
    private LocalDate expiryDate;

    private String planDocumentUrl;

    private BookingStatus status;
}