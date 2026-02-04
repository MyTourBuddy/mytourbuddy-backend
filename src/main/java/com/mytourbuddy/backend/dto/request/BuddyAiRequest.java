package com.mytourbuddy.backend.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuddyAiRequest {
    @NotBlank(message = "FirstName is required")
    private String firstName;
    @NotBlank(message = "LastName is required")
    private String lastName;
    private Integer age;
    private List<String> travelPrefs;

    @NotBlank(message = "Start location is required")
    private String startLocation;

    @NotBlank(message = "End destination is required")
    private String endDestination;
}
