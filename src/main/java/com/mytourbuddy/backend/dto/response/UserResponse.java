package com.mytourbuddy.backend.dto.response;

import com.mytourbuddy.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

}