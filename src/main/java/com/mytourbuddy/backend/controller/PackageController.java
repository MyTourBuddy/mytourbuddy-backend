package com.mytourbuddy.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mytourbuddy.backend.dto.request.CreatePackageRequest;
import com.mytourbuddy.backend.dto.request.UpdatePackageRequest;
import com.mytourbuddy.backend.dto.response.PackageResponse;
import com.mytourbuddy.backend.security.CustomUserDetails;
import com.mytourbuddy.backend.service.PackageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

    @Autowired
    private PackageService service;

    @GetMapping
    public ResponseEntity<List<PackageResponse>> getAllPackages() {
        List<PackageResponse> packages = service.getAllPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> getPackageById(@PathVariable String id) {
        Optional<PackageResponse> packages = service.getPackageById(id);
        return packages.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guides/{guideId}")
    public ResponseEntity<List<PackageResponse>> getPackagesByGuideId(@PathVariable String guideId) {
        List<PackageResponse> packages = service.getPackagesByGuideId(guideId);
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PackageResponse>> searchPackages(@RequestParam(required = false) String q) {
        if (q != null && !q.isEmpty()) {
            return ResponseEntity.ok(service.searchPackages(q));
        }
        return ResponseEntity.ok(service.getAllPackages());
    }

    @PostMapping
    public ResponseEntity<PackageResponse> createPackage(@Valid @RequestBody CreatePackageRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String guideId = userDetails.getUserId();

        PackageResponse created = service.createPackage(request, guideId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageResponse> updatePackage(@PathVariable String id,
            @Valid @RequestBody UpdatePackageRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userId = userDetails.getUserId();

        try {
            PackageResponse updated = service.updatePackage(id, request, userId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable String id) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String userId = userDetails.getUserId();

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        try {
            service.deletePackageById(id, isAdmin ? null : userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
