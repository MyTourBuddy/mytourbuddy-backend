package com.mytourbuddy.backend.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "packages")
public class Package {
    @Id
    private String id;

    private String guideId;
    private String title;
    private String description;
    private Double price;
    private String duration;
    private String location;
    private String image;
    private Integer maxGroupSize;
    private List<String> included;
    private List<String> notIncluded;
    private String note;
    private String status;
    private Instant createdAt;
}
