package com.mytourbuddy.backend.dto.response;

import java.time.Instant;
import java.util.List;

import com.mytourbuddy.backend.model.PackageStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageResponse {
    private String id;
    private String guideId;
    private String title;
    private String description;
    private Double price;
    private Integer duration;
    private String location;
    private String image;
    private Integer maxGroupSize;
    private List<String> included;
    private List<String> notIncluded;
    private String note;
    private PackageStatus status;
    private Instant createdAt;
}
