package com.mytourbuddy.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mytourbuddy.backend.model.Package;

public interface PackageRepository extends MongoRepository<Package, String> {
    List<Package> findByGuideId(String guideId);

    List<Package> findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(String title, String location);
}
