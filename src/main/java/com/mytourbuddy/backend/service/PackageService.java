package com.mytourbuddy.backend.service;

import com.mytourbuddy.backend.model.Package;
import com.mytourbuddy.backend.repository.PackageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    public Optional<Package> getPackageById(String id) {
        return packageRepository.findById(id);
    }

    public List<Package> getPackagesByGuideId(String guideId) {
        return packageRepository.findByGuideId(guideId);
    }

    public Package createPackage(Package pkg) {
        pkg.setStatus("CREATED");
        pkg.setCreatedAt(Instant.now());
        return packageRepository.save(pkg);
    }

    public Package updatePackage(String id, Package pkg) {
        Package existingPkg = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        existingPkg.setTitle(pkg.getTitle());
        existingPkg.setDescription(pkg.getDescription());
        existingPkg.setPrice(pkg.getPrice());
        existingPkg.setDuration(pkg.getDuration());
        existingPkg.setLocation(pkg.getLocation());
        existingPkg.setImage(pkg.getImage());
        existingPkg.setMaxGroupSize(pkg.getMaxGroupSize());
        existingPkg.setIncluded(pkg.getIncluded());
        existingPkg.setNotIncluded(pkg.getNotIncluded());
        existingPkg.setNote(pkg.getNote());

        return packageRepository.save(existingPkg);
    }

    public void deletePackageById(String id) {
        if (!packageRepository.existsById(id)) {
            throw new IllegalArgumentException("Package not found");
        }
        packageRepository.deleteById(id);
    }
}
