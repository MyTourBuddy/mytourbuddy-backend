package com.mytourbuddy.backend.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Guide ID is required")
    private String guideId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotBlank(message = "Duration is required")
    private String duration;

    @NotBlank(message = "Location is required")
    private String location;

    private String image;
    private Integer maxGroupSize;

    @NotEmpty(message = "Included items are required")
    private List<String> included;

    private List<String> notIncluded;
    private String note;
    private PackageStatus status;
    private Instant createdAt;
}
