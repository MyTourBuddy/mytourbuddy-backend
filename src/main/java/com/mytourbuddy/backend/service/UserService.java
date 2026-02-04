package com.mytourbuddy.backend.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.dto.request.RegisterRequest;
import com.mytourbuddy.backend.dto.request.UpdateRequest;
import com.mytourbuddy.backend.dto.response.UserResponse;
import com.mytourbuddy.backend.mapper.UserMapper;
import com.mytourbuddy.backend.model.Role;
import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // get all users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    // get user by id
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    // get user by username
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return userMapper.toResponse(user);
    }

    // create user
    public UserResponse createUser(RegisterRequest request) {
        // check username and password already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // validate role specific fields
        validateRoleSpecificFields(request);

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setIsProfileComplete(false);
        user.setMemberSince(Instant.now());

        if (request.getRole() == Role.GUIDE) {
            user.setIsVerified(false);
        }

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    // update user details
    public UserResponse updateUser(String id, UpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        checkUpdateAuthorization(existingUser);

        userMapper.updateEntityFromRequest(request, existingUser);

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toResponse(updatedUser);
    }

    // delete user by id
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // validate role specific fields
    private void validateRoleSpecificFields(RegisterRequest request) {
        if (request.getRole() == Role.TOURIST) {
            if (request.getCountry() == null || request.getCountry().isEmpty()) {
                throw new IllegalArgumentException("Country is required for tourists");
            }
            if (request.getTravelPreferences() == null || request.getTravelPreferences().isEmpty()) {
                throw new IllegalArgumentException("Travel preferences are required for tourists");
            }
        }

        if (request.getRole() == Role.GUIDE) {
            if (request.getLanguages() == null || request.getLanguages().isEmpty()) {
                throw new IllegalArgumentException("Languages are required for guides");
            }
            if (request.getYearsOfExp() == null || request.getYearsOfExp() < 0) {
                throw new IllegalArgumentException("Years of experience is required for guides");
            }
        }
    }

    // allow update user if admin or logged user
    private void checkUpdateAuthorization(User userToUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !userToUpdate.getUsername().equals(currentUsername)) {
            throw new SecurityException("You are not authorized to update this profile");
        }
    }
}
