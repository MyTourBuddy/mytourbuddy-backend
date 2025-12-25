package com.mytourbuddy.backend.service;

import com.mytourbuddy.backend.model.Package;
import com.mytourbuddy.backend.repository.PackageRepository;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    // get all packages
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    // get package by id
    public Optional<Package> getPackageById(String id) {
        return packageRepository.findById(id);
    }

    // get packages by guide id
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

        copyNonNullProperties(pkg, existingPkg);

        return packageRepository.save(existingPkg);
    }

    private void copyNonNullProperties(Package source, Package target) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        for (PropertyDescriptor pd : pds) {
            String propName = pd.getName();
            Object srcValue = src.getPropertyValue(propName);

            if (srcValue != null && !"class".equals(propName)) {
                new BeanWrapperImpl(target).setPropertyValue(propName, srcValue);
            }
        }
    }

    public void deletePackageById(String id) {
        if (!packageRepository.existsById(id)) {
            throw new IllegalArgumentException("Package not found");
        }
        packageRepository.deleteById(id);
    }
}
