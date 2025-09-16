package com.deadpr.backend.repository;

import com.deadpr.backend.model.TrainingPackage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrainingPackageRepository extends MongoRepository<TrainingPackage, String> {

    List<TrainingPackage> findByTrainerId(String trainerId);
}