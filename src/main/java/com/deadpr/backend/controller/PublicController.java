package com.deadpr.backend.controller;

import com.deadpr.backend.model.Trainer;
import com.deadpr.backend.model.TrainingPackage;
import com.deadpr.backend.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private PublicService publicService;
    @GetMapping("/trainers")
    public ResponseEntity<List<Trainer>> getAllTrainers() {
        List<Trainer> trainers = publicService.getAllTrainers();
        return ResponseEntity.ok(trainers);
    }

    @GetMapping("/trainers/{trainerId}")
    public ResponseEntity<Trainer> getTrainerById(@PathVariable String trainerId) {
        Trainer trainer = publicService.getTrainerById(trainerId);
        return ResponseEntity.ok(trainer);
    }

    @GetMapping("/trainers/{trainerId}/packages")
    public ResponseEntity<List<TrainingPackage>> getPackagesByTrainerId(@PathVariable String trainerId) {
        List<TrainingPackage> packages = publicService.getPackagesByTrainerId(trainerId);
        return ResponseEntity.ok(packages);
    }
}