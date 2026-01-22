package com.mytourbuddy.backend.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.dto.request.LoginRequest;
import com.mytourbuddy.backend.dto.request.RegisterRequest;
import com.mytourbuddy.backend.dto.response.AuthResponse;
import com.mytourbuddy.backend.dto.response.UserResponse;
import com.mytourbuddy.backend.mapper.UserMapper;
import com.mytourbuddy.backend.model.Role;
import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.UserRepository;
import com.mytourbuddy.backend.security.JwtUtil;

import io.jsonwebtoken.JwtException;

@Service
public class AuthService {
        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private UserMapper userMapper;

        @Autowired
        private PasswordEncoder passwordEncoder;

        // login user
        public AuthResponse login(LoginRequest request) {
                try {
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        request.getUsername(),
                                                        request.getPassword()));

                        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                        User user = userRepository.findByUsername(request.getUsername())
                                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

                        UserResponse userResponse = userMapper.toResponse(user);

                        String token = jwtUtil.generateToken(userDetails);

                        return new AuthResponse(token, userResponse);
                } catch (BadCredentialsException e) {
                        throw new IllegalArgumentException("Invalid username or password");
                }
        }

        // register user
        public AuthResponse register(RegisterRequest request) {
                if (request.getRole() == Role.ADMIN) {
                        throw new IllegalArgumentException(
                                        "Admin registration requires admin privileges. Use /register-admin endpoint.");
                }

                if (userRepository.existsByUsername(request.getUsername())) {
                        throw new IllegalArgumentException("Username already exists");
                }

                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new IllegalArgumentException("Email already exists");
                }

                validateRoleSpecificFields(request);

                User user = userMapper.toEntity(request);

                user.setPassword(passwordEncoder.encode(request.getPassword()));

                if (request.getRole() == Role.ADMIN) {
                        user.setIsProfileComplete(true);
                } else {
                        user.setIsProfileComplete(false);
                }

                user.setMemberSince(Instant.now());

                if (request.getRole() == Role.GUIDE) {
                        user.setIsVerified(false);
                }

                User savedUser = userRepository.save(user);

                UserResponse userResponse = userMapper.toResponse(savedUser);

                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                                .username(savedUser.getUsername())
                                .password(savedUser.getPassword())
                                .authorities("ROLE_" + savedUser.getRole().name())
                                .build();

                String token = jwtUtil.generateToken(userDetails);

                return new AuthResponse(token, userResponse);
        }

        // register admin
        public UserResponse registerAdmin(RegisterRequest request) {
                if (userRepository.existsByUsername(request.getUsername())) {
                        throw new IllegalArgumentException("Username already exists");
                }

                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new IllegalArgumentException("Email already exists");
                }
                User user = userMapper.toEntity(request);

                user.setPassword(passwordEncoder.encode(request.getPassword()));

                user.setIsProfileComplete(true);
                user.setMemberSince(Instant.now());

                User savedUser = userRepository.save(user);

                return userMapper.toResponse(savedUser);
        }

        public UserResponse getUserFromToken(String token) {
                try {
                        String username = jwtUtil.extractUsername(token);

                        if (username == null) {
                                throw new IllegalArgumentException("Invalid token");
                        }

                        // Validate token is not expired
                        User user = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

                        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                                        .username(user.getUsername())
                                        .password(user.getPassword())
                                        .authorities("ROLE_" + user.getRole().name())
                                        .build();

                        if (!jwtUtil.isTokenValid(token, userDetails)) {
                                throw new IllegalArgumentException("Token is invalid or expired");
                        }

                        return userMapper.toResponse(user);
                } catch (IllegalArgumentException | JwtException e) {
                        throw new IllegalArgumentException("Invalid or expired token");
                }
        }

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
}