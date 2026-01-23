package com.mytourbuddy.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mytourbuddy.backend.model.Package;
import com.mytourbuddy.backend.service.PackageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

    @Autowired
    private PackageService service;

    @GetMapping
    public ResponseEntity<List<Package>> getAllPackages() {
        List<Package> packages = service.getAllPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Package> getPackageById(@PathVariable String id) {
        Optional<Package> packages = service.getPackageById(id);
        return packages.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guides/{guideId}")
    public ResponseEntity<List<Package>> getPackagesByGuideId(@PathVariable String guideId) {
        List<Package> packages = service.getPackagesByGuideId(guideId);
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Package>> searchPackages(@RequestParam(required = false) String q) {
        if (q != null && !q.isEmpty()) {
            return ResponseEntity.ok(service.searchPackages(q));
        }
        return ResponseEntity.ok(service.getAllPackages());
    }

    @PostMapping
    public ResponseEntity<Package> createPackage(@Valid @RequestBody Package pkg) {
        Package created = service.createPackage(pkg);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Package> updatePackage(@PathVariable String id, @RequestBody Package pkg) {
        try {
            Package updated = service.updatePackage(id, pkg);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable String id) {
        try {
            service.deletePackageById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
