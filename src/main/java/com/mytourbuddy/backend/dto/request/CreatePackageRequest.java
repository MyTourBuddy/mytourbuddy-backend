package com.mytourbuddy.backend.dto.request;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePackageRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1")
    private Integer duration;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Image is required")
    private String image;

    @NotNull(message = "Group size is required")
    @Positive(message = "Group size must be positive")
    private Integer maxGroupSize;

    @NotEmpty(message = "Included items are required")
    private List<String> included;

    private List<String> notIncluded;
    private String note;
}
