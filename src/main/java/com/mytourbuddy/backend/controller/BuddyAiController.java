package com.mytourbuddy.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytourbuddy.backend.dto.request.BuddyAiRequest;
import com.mytourbuddy.backend.dto.response.BuddyAiResponse;
import com.mytourbuddy.backend.service.BuddyAiService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/buddy-ai")
public class BuddyAiController {
    @Autowired
    private BuddyAiService buddyAiService;

    @PostMapping
    public ResponseEntity<BuddyAiResponse> generateGuide(@Valid @RequestBody BuddyAiRequest request) {
        BuddyAiResponse response = buddyAiService.generateGuide(request);
        return ResponseEntity.ok(response);
    }
}
