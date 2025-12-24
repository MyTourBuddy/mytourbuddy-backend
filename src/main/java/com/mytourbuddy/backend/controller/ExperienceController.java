package com.mytourbuddy.backend.controller;

import com.mytourbuddy.backend.model.Experience;
import com.mytourbuddy.backend.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/experiences")
public class ExperienceController {

    @Autowired
    private ExperienceService service;

    @GetMapping
    public List<Experience> getAllExperiences() {
        return service.getAllExperiences();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Experience> getExperienceById(@PathVariable String id) {
        return service.getExperienceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Experience createExperience(@RequestBody Experience experience) {
        return service.createExperience(experience);
    }

    @PutMapping("/{id}")
    public Experience updateExperience(@PathVariable String id, @RequestBody Experience experience) {
        return service.updateExperience(id, experience);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable String id) {
        service.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }
}
