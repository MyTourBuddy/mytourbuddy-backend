package com.mytourbuddy.backend.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TouristResponse extends UserResponse {
    private String country;
    private List<String> travelPreferences;
    private List<String> preferredDestinations;
    private List<String> travelInterests;
    private List<String> languageSpoken;
}
