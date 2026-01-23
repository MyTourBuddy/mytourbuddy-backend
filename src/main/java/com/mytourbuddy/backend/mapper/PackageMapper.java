package com.mytourbuddy.backend.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.mytourbuddy.backend.dto.request.CreatePackageRequest;
import com.mytourbuddy.backend.dto.request.UpdatePackageRequest;
import com.mytourbuddy.backend.dto.response.PackageResponse;
import com.mytourbuddy.backend.model.Package;
import com.mytourbuddy.backend.model.PackageStatus;

@Component
public class PackageMapper {
    public Package toEntity(CreatePackageRequest request, String guideId) {
        Package pkg = new Package();

        pkg.setGuideId(guideId);
        pkg.setTitle(request.getTitle());
        pkg.setDescription(request.getDescription());
        pkg.setPrice(request.getPrice());
        pkg.setDuration(request.getDuration());
        pkg.setLocation(request.getLocation());
        pkg.setImage(request.getImage());
        pkg.setMaxGroupSize(request.getMaxGroupSize());
        pkg.setIncluded(request.getIncluded());
        pkg.setNotIncluded(request.getNotIncluded());
        pkg.setNote(request.getNote());
        pkg.setStatus(PackageStatus.ACTIVE);
        pkg.setCreatedAt(Instant.now());

        return pkg;
    }

    public PackageResponse toResponse(Package pkg) {
        PackageResponse response = new PackageResponse();

        response.setId(pkg.getId());
        response.setTitle(pkg.getTitle());
        response.setDescription(pkg.getDescription());
        response.setPrice(pkg.getPrice());
        response.setDuration(pkg.getDuration());
        response.setLocation(pkg.getLocation());
        response.setImage(pkg.getImage());
        response.setMaxGroupSize(pkg.getMaxGroupSize());
        response.setIncluded(pkg.getIncluded());
        response.setNotIncluded(pkg.getNotIncluded());
        response.setNote(pkg.getNote());
        response.setStatus(pkg.getStatus());
        response.setCreatedAt(pkg.getCreatedAt());

        return response;
    }

    public void updateEntityFromRequest(UpdatePackageRequest request, Package pkg) {
        if (request.getTitle() != null) {
            pkg.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            pkg.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            pkg.setPrice(request.getPrice());
        }
        if (request.getDuration() != null) {
            pkg.setDuration(request.getDuration());
        }
        if (request.getLocation() != null) {
            pkg.setLocation(request.getLocation());
        }
        if (request.getImage() != null) {
            pkg.setImage(request.getImage());
        }
        if (request.getMaxGroupSize() != null) {
            pkg.setMaxGroupSize(request.getMaxGroupSize());
        }
        if (request.getIncluded() != null) {
            pkg.setIncluded(request.getIncluded());
        }
        if (request.getNotIncluded() != null) {
            pkg.setNotIncluded(request.getNotIncluded());
        }
        if (request.getNote() != null) {
            pkg.setNote(request.getNote());
        }
        if (request.getStatus() != null) {
            pkg.setStatus(request.getStatus());
        }
    }
}
