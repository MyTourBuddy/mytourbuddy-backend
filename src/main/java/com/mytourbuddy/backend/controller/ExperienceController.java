package com.mytourbuddy.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mytourbuddy.backend.model.Experience;
import com.mytourbuddy.backend.service.ExperienceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/experiences")
public class ExperienceController {

    @Autowired
    private ExperienceService service;

    @GetMapping
    public ResponseEntity<List<Experience>> getAllExperiences() {
        List<Experience> experiences = service.getAllExperiences();
        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Experience> getExperienceById(@PathVariable String id) {
        Optional<Experience> experience = service.getExperienceById(id);
        return experience.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guides/{guideId}")
    public ResponseEntity<List<Experience>> getExperiencesByGuideId(@PathVariable String guideId) {
        List<Experience> experiences = service.getExperiencesByGuideId(guideId);
        return ResponseEntity.ok(experiences);
    }

    @PostMapping
    public ResponseEntity<Experience> createExperience(@Valid @RequestBody Experience experience) {
        Experience created = service.createExperience(experience);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Experience> updateExperience(@PathVariable String id, @RequestBody Experience experience) {
        try {
            Experience updated = service.updateExperience(id, experience);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable String id) {
        try {
            service.deleteExperience(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
