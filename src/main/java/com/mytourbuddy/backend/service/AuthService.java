package com.mytourbuddy.backend.service;

import com.mytourbuddy.backend.dto.request.LoginRequest;
import com.mytourbuddy.backend.dto.response.AuthResponse;
import com.mytourbuddy.backend.dto.response.UserResponse;
import com.mytourbuddy.backend.mapper.UserMapper;
import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.UserRepository;
import com.mytourbuddy.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtUtil.generateToken(userDetails);
        UserResponse userResponse = userMapper.toResponse(user);

        return new AuthResponse(token, userResponse);
    }
}