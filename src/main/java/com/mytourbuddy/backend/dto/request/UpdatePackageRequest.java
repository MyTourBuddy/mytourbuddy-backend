package com.mytourbuddy.backend.dto.request;

import java.util.List;

import com.mytourbuddy.backend.model.PackageStatus;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePackageRequest {
    private String title;
    private String description;

    @Positive(message = "Price must be positive")
    private Double price;

    private String duration;
    private String location;
    private String image;

    @Positive(message = "Group size must be positive")
    private Integer maxGroupSize;

    private List<String> included;
    private List<String> notIncluded;
    private String note;
    private PackageStatus status;
}
