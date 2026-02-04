package com.mytourbuddy.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.dto.request.CreatePackageRequest;
import com.mytourbuddy.backend.dto.request.UpdatePackageRequest;
import com.mytourbuddy.backend.dto.response.PackageResponse;
import com.mytourbuddy.backend.mapper.PackageMapper;
import com.mytourbuddy.backend.model.Package;
import com.mytourbuddy.backend.repository.PackageRepository;
import com.mytourbuddy.backend.repository.UserRepository;
import com.mytourbuddy.backend.util.IdGenerator;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private PackageMapper packageMapper;

    // get all packages
    public List<PackageResponse> getAllPackages() {
        return packageRepository.findAll().stream()
                .map(packageMapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    // get package by id
    public Optional<PackageResponse> getPackageById(String id) {
        return packageRepository.findById(id).map(packageMapper::toResponse);
    }

    // get packages by guide id
    public List<PackageResponse> getPackagesByGuideId(String guideId) {
        return packageRepository.findByGuideId(guideId).stream()
                .map(packageMapper::toResponse)
                .collect(Collectors.toList());
    }

    // search packages by title or location
    public List<PackageResponse> searchPackages(String query) {
        return packageRepository.findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(query, query)
                .stream()
                .map(packageMapper::toResponse)
                .collect(Collectors.toList());
    }

    // create package
    public PackageResponse createPackage(CreatePackageRequest request, String guideId) {
        if (request == null) {
            throw new IllegalArgumentException("Package is required");
        }

        boolean guideExists = userRepository.existsById(guideId);
        if (!guideExists) {
            throw new IllegalArgumentException("Guide with id " + guideId + " not found");
        }

        Package newPackage = packageMapper.toEntity(request, guideId);
        newPackage.setId(idGenerator.generate("pkg", packageRepository::existsById));

        Package savedPackage = packageRepository.save(newPackage);
        return packageMapper.toResponse(savedPackage);
    }

    // update package
    public PackageResponse updatePackage(String id, UpdatePackageRequest request, String userId) {
        Package existingPkg = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        if (!existingPkg.getGuideId().equals(userId)) {
            throw new SecurityException("You can only update your own packages");
        }

        packageMapper.updateEntityFromRequest(request, existingPkg);

        Package savedPackage = packageRepository.save(existingPkg);
        return packageMapper.toResponse(savedPackage);
    }

    // delete package
    public void deletePackageById(String id, String userId) {
        Package existingPkg = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        if (userId != null && !existingPkg.getGuideId().equals(userId)) {
            throw new SecurityException("You can only delete your own packages");
        }

        packageRepository.deleteById(id);
    }
}
