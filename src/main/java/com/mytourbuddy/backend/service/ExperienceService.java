package com.mytourbuddy.backend.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.model.Experience;
import com.mytourbuddy.backend.repository.ExperienceRepository;
import com.mytourbuddy.backend.repository.UserRepository;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository repository;

    @Autowired
    private UserRepository userRepository;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 6;
    private static final String ID_PREFIX = "exp";
    private static final SecureRandom random = new SecureRandom();

    // get all experiences
    public List<Experience> getAllExperiences() {
        return repository.findAll();
    }

    // get experience by id
    public Optional<Experience> getExperienceById(String id) {
        return repository.findById(id);
    }

    // get experiences by guide id
    public List<Experience> getExperiencesByGuideId(String guideId) {
        return repository.findByGuideId(guideId);
    }

    // create experience
    public Experience createExperience(Experience experience) {
        if (experience == null) {
            throw new IllegalArgumentException("Experience is required");
        }

        boolean guideExists = userRepository.existsById(experience.getGuideId());

        if (!guideExists) {
            throw new IllegalArgumentException("Guide with id " + experience.getGuideId() + " not found");
        }

        experience.setId(generateExperienceId());
        experience.setCreatedAt(Instant.now());
        return repository.save(experience);
    }

    private String generateExperienceId() {
        String experienceId;
        int maxAttempts = 10;
        int attempts = 0;

        do {
            StringBuilder sb = new StringBuilder(ID_PREFIX);
            for (int i = 0; i < ID_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }
            experienceId = sb.toString();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException(
                        "Failed to generate unique experience ID after " + maxAttempts + " attempts");
            }
        } while (repository.existsById(experienceId));

        return experienceId;
    }

    // update experience
    public Experience updateExperience(String id, Experience updatedExperience) {
        Experience existingExperience = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found"));

        Optional.ofNullable(updatedExperience.getTitle())
                .filter(title -> !title.isEmpty())
                .ifPresent(existingExperience::setTitle);

        Optional.ofNullable(updatedExperience.getDescription())
                .filter(desc -> !desc.isEmpty())
                .ifPresent(existingExperience::setDescription);

        Optional.ofNullable(updatedExperience.getImage())
                .ifPresent(existingExperience::setImage);

        Optional.ofNullable(updatedExperience.getExperiencedAt())
                .ifPresent(existingExperience::setExperiencedAt);

        return repository.save(existingExperience);
    }

    // delete experience
    public void deleteExperience(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Experience not found");
        }
        repository.deleteById(id);
    }
}
