package com.mytourbuddy.backend.service;

import com.mytourbuddy.backend.dto.request.LoginRequest;
import com.mytourbuddy.backend.dto.request.RegisterRequest;
import com.mytourbuddy.backend.dto.response.AuthResponse;
import com.mytourbuddy.backend.dto.response.UserResponse;
import com.mytourbuddy.backend.mapper.UserMapper;
import com.mytourbuddy.backend.model.Role;
import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.UserRepository;
import com.mytourbuddy.backend.security.JwtUtil;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        AuthService(PasswordEncoder passwordEncoder) {
                this.passwordEncoder = passwordEncoder;
        }

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
                if (userRepository.existsByUsername(request.getUsername())) {
                        throw new IllegalArgumentException("Username already exists");
                }

                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new IllegalArgumentException("Email already exists");
                }

                validateRoleSpecificFields(request);

                User user = userMapper.toEntity(request);

                user.setPassword(passwordEncoder.encode(request.getPassword()));

                user.setIsProfileComplete(false);
                user.setMemberSince(Instant.now());

                if (request.getRole() == Role.GUIDE) {
                        user.setIsVerified(false);
                }

                User savedUser = userRepository.save(user);

                UserResponse userResponse = userMapper.toResponse(savedUser);

                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                                .username(savedUser.getUsername())
                                .password(savedUser.getPassword()) // Already hashed
                                .authorities("ROLE_" + savedUser.getRole().name())
                                .build();

                String token = jwtUtil.generateToken(userDetails);

                return new AuthResponse(token, userResponse);
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