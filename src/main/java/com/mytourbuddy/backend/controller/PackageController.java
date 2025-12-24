package com.mytourbuddy.backend.controller;

import com.mytourbuddy.backend.model.Package;
import com.mytourbuddy.backend.service.PackageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public List<Package> getAllPackages() {
        return packageService.getAllPackages();
    }

    @GetMapping("/{id}")
    public Package getPackageById(@PathVariable String id) {
        return packageService.getPackageById(id).orElse(null);
    }

    @GetMapping("/guide/{guideId}")
    public List<Package> getPackagesByGuideId(@PathVariable String guideId) {
        return packageService.getPackagesByGuideId(guideId);
    }

    @PostMapping
    public Package createPackage(@RequestBody Package pkg) {
        return packageService.createPackage(pkg);
    }

    @PutMapping("/{id}")
    public Package updatePackage(@PathVariable String id, @RequestBody Package pkg) {
        return packageService.updatePackage(id, pkg);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable String id) {
        packageService.deletePackageById(id);
        return ResponseEntity.noContent().build();
    }
}
