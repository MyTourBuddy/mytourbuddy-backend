package com.mytourbuddy.backend.service;

import com.mytourbuddy.backend.model.Experience;
import com.mytourbuddy.backend.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository repository;

    public List<Experience> getAllExperiences() {
        return repository.findAll();
    }

    public Optional<Experience> getExperienceById(String id) {
        return repository.findById(id);
    }

    public Experience createExperience(Experience experience) {
        experience.setCreatedAt(new java.util.Date());
        return repository.save(experience);
    }

    public Experience updateExperience(String id, Experience experience) {
        return repository.findById(id)
                .map(existing -> {
                    if (experience.getGuideId() != null) existing.setGuideId(experience.getGuideId());
                    if (experience.getTitle() != null) existing.setTitle(experience.getTitle());
                    if (experience.getDescription() != null) existing.setDescription(experience.getDescription());
                    if (experience.getImage() != null) existing.setImage(experience.getImage());
                    if (experience.getExperiencedAt() != null) existing.setExperiencedAt(experience.getExperiencedAt());
                    return repository.save(existing);
                }).orElseThrow(() -> new RuntimeException("Experience not found with id " + id));
    }


    public void deleteExperience(String id) {
        repository.deleteById(id);
    }
}
