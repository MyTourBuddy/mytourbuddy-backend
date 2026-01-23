package com.mytourbuddy.backend.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.dto.request.CreateExperienceRequest;
import com.mytourbuddy.backend.model.Experience;
import com.mytourbuddy.backend.repository.ExperienceRepository;
import com.mytourbuddy.backend.repository.UserRepository;
import com.mytourbuddy.backend.util.IdGenerator;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdGenerator idGenerator;

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
    public Experience createExperience(CreateExperienceRequest request, String guideId) {
        if (request == null) {
            throw new IllegalArgumentException("Request is required");
        }

        boolean guideExists = userRepository.existsById(guideId);
        if (!guideExists) {
            throw new IllegalArgumentException("Guide not found");
        }

        Experience experience = new Experience();
        experience.setGuideId(guideId);
        experience.setTitle(request.getTitle());
        experience.setDescription(request.getDescription());
        experience.setImage(request.getImage());
        experience.setExperiencedAt(request.getExperiencedAt());
        experience.setId(idGenerator.generate("exp", repository::existsById));
        experience.setCreatedAt(Instant.now());

        return repository.save(experience);
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
    public void deleteExperience(String id, String userId) {
        Experience experience = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Experience not found"));

        if (userId != null && !experience.getGuideId().equals(userId)) {
            throw new SecurityException("You can only delete your own experiences");
        }

        repository.deleteById(id);
    }
}
