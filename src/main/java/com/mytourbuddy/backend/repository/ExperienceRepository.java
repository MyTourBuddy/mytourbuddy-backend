package com.mytourbuddy.backend.repository;

import com.mytourbuddy.backend.model.Experience;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends MongoRepository<Experience, String> {
    List<Experience> findByGuideId(String guideId);
}
