package com.mytourbuddy.backend.dto.request;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Min(value = 18, message = "Must be at least 18 years old")
    @Max(value = 150, message = "Age must be realistic")
    private Integer age;

    private String avatar;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be valid")
    private String phone;

    // tourist only
    private String country;
    private List<String> travelPreferences;
    private List<String> preferredDestinations;
    private List<String> travelInterests;
    private List<String> languagesSpoken;

    // guide only
    private List<String> languages;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExp;

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    private String bio;

    private List<String> specializations;
    private List<String> certifications;
    private String emergencyContact;
    private String website;
    private List<String> socialMedia;
}