package com.mytourbuddy.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mytourbuddy.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

// todo: update with roles
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserResponse {
    private String id;
    private Role role;

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Integer age;
    private String avatar;
    private String phone;
    private Boolean isProfileComplete;
    private Instant memberSince;

    // tourist only
    private String country;
    private List<String> travelPreferences;
    private List<String> preferredDestinations;
    private List<String> travelInterests;
    private List<String> languageSpoken;

    // guide only
    private List<String> languages;
    private Integer yearsOfExp;
    private String bio;
    private List<String> specializations;
    private String certifications;
    private String emergencyContact;
    private String website;
    private List<String> socialMedia;
    private Boolean isVerified;
}