package com.mytourbuddy.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.mytourbuddy.backend.dto.request.BuddyAiRequest;
import com.mytourbuddy.backend.dto.response.BuddyAiResponse;

@Service
public class BuddyAiService {
    private final Client client;

    public BuddyAiService(@Value("${gemini.api.key}") String apiKey) {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public BuddyAiResponse generateGuide(BuddyAiRequest request) {
        try {
            String prompt = "Generate a tourist guide for " + request.getFirstName() + " " + request.getLastName() +
                    ", age " + request.getAge() + ", preferences: " + request.getTravelPrefs() +
                    ", from " + request.getStartLocation() + " to " + request.getEndDestination() + " around 50 words without thinking.";

            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", prompt, null);
            String guide = response.text();
            return new BuddyAiResponse(guide);
        } catch (Exception e) {
            // Log the error and throw a custom exception
            throw new IllegalArgumentException("Location not supported: " + e.getMessage());
        }
    }
}
