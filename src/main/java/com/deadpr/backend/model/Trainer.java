package com.deadpr.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "trainers")
public class Trainer {

    @Id
    private String id;

    private String description;
    private String profileImageUrl;
    private List<String> specializations;

    @DBRef
    private User user;
}