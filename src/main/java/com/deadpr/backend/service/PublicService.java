package com.deadpr.backend.service;

import com.deadpr.backend.model.Trainer;
import com.deadpr.backend.model.TrainingPackage;

import java.util.List;

public interface PublicService {
    List<Trainer> getAllTrainers();
    Trainer getTrainerById(String trainerId);
    List<TrainingPackage> getPackagesByTrainerId(String trainerId);
}