package com.deadpr.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "packages")
public class TrainingPackage {

    @Id
    private String id;

    private String name;
    private String description;
    private double price;
    private int durationInDays;

    @DBRef
    private Trainer trainer;
}