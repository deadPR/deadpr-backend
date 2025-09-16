package com.deadpr.backend.repository;

import com.deadpr.backend.model.Booking;
import com.deadpr.backend.model.BookingStatus;
import com.deadpr.backend.model.TrainingPackage;
import com.deadpr.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findByClient(User client);
    List<Booking> findByTrainerId(String trainerId);
    List<Booking> findByTrainer_User_Id(String userId);
    boolean existsByClientAndTrainingPackageAndStatus(User client, TrainingPackage trainingPackage, BookingStatus status);
}