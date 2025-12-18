package com.mytourbuddy.backend.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private Role role;

    // base user
    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String username;
    
    private String password;
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
    private Double dailyRate;
    private Integer maxGroupSize;
    private String transportMode;
    private List<String> ageGroups;
    private List<String> workingDays;
    private String emergencyContact;
    private String website;
    private List<String> socialMedia;
}