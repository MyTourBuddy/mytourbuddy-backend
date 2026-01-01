package com.mytourbuddy.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mytourbuddy.backend.dto.request.LoginRequest;
import com.mytourbuddy.backend.dto.request.RegisterRequest;
import com.mytourbuddy.backend.dto.response.AuthResponse;
import com.mytourbuddy.backend.dto.response.UserResponse;
import com.mytourbuddy.backend.repository.UserRepository;
import com.mytourbuddy.backend.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${jwt.cookie.name}")
    private String cookieName;

    @Value("${jwt.cookie.max-age}")
    private int cookieMaxAge;

    @Value("${jwt.cookie.secure}")
    private boolean cookieSecure;

    @Autowired
    private UserRepository userRepository;

    private Cookie createJwtCookie(String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(cookieMaxAge);
        return cookie;
    }

    private Cookie clearJwtCookie() {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    // register user
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {
        try {
            AuthResponse authResponse = authService.register(request);

            Cookie jwtCookie = createJwtCookie(authResponse.getToken());
            response.addCookie(jwtCookie);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("user", authResponse.getUser());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    // login user
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);

        Cookie jwtCookie = createJwtCookie(authResponse.getToken());
        response.addCookie(jwtCookie);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("user", authResponse.getUser());

        return ResponseEntity.ok(responseBody);
    }

    // verify current session
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @CookieValue(name = "token", required = false) String token) {

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UserResponse user = authService.getUserFromToken(token);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("user", user);
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // logout
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        Cookie jwtCookie = clearJwtCookie();
        response.addCookie(jwtCookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logged out successfully");
        return ResponseEntity.ok(responseBody);
    }

    // check username availability
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        boolean exists = userRepository.existsByUsername(username);
        Map<String, Object> response = new HashMap<>();
        response.put("available", !exists);
        response.put("message", exists ? "Username is already taken" : "Username is available");
        return ResponseEntity.ok(response);
    }

    // check email availability
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("available", !exists);
        response.put("message", exists ? "Email is already taken" : "Email is available");
        return ResponseEntity.ok(response);
    }

    // health check
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Authentication service is running");
        response.put("service", "MyTourBuddy Auth API");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
}