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
public class GuideResponse extends UserResponse {
    private List<String> languages;
    private Integer yearsOfExp;
    private String bio;
    private List<String> specializations;
    private List<String> certifications;
    private String emergencyContact;
    private String website;
    private List<String> socialMedia;
    private Boolean isVerified;
}
