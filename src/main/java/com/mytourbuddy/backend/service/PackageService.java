package com.mytourbuddy.backend.service;

import java.beans.PropertyDescriptor;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.model.Package;
import com.mytourbuddy.backend.model.PackageStatus;
import com.mytourbuddy.backend.repository.PackageRepository;
import com.mytourbuddy.backend.repository.UserRepository;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Set<String> IMMUTABLE_FIELDS = Set.of("id", "guideId", "createdAt");
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 6;
    private static final String ID_PREFIX = "pkg";
    private static final SecureRandom random = new SecureRandom();

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

    // create package
    public Package createPackage(Package pkg) {
        if (pkg == null) {
            throw new IllegalArgumentException("Package is required");
        }

        boolean guideExists = userRepository.existsById(pkg.getGuideId());
        if (!guideExists) {
            throw new IllegalArgumentException("Guide with id " + pkg.getGuideId() + " not found");
        }

        pkg.setId(generatePackageId());
        pkg.setStatus(PackageStatus.ACTIVE);
        pkg.setCreatedAt(Instant.now());
        return packageRepository.save(pkg);
    }

    private String generatePackageId() {
        String packageId;
        int maxAttempts = 10;
        int attempts = 0;

        do {
            StringBuilder sb = new StringBuilder(ID_PREFIX);
            for (int i = 0; i < ID_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }
            packageId = sb.toString();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("Failed to generate unique package ID after " + maxAttempts + " attempts");
            }
        } while (packageRepository.existsById(packageId));

        return packageId;
    }

    // update package
    public Package updatePackage(String id, Package pkg) {
        Package existingPkg = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        validateImmutableFields(pkg);

        copyNonNullProperties(pkg, existingPkg);

        return packageRepository.save(existingPkg);
    }

    private void validateImmutableFields(Package pkg) {
        BeanWrapper src = new BeanWrapperImpl(pkg);

        for (String field : IMMUTABLE_FIELDS) {
            Object value = src.getPropertyValue(field);
            if (value != null) {
                throw new IllegalArgumentException(
                        "Field '" + field + "' cannot be updated");
            }
        }
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

    // delete package
    public void deletePackageById(String id) {
        if (!packageRepository.existsById(id)) {
            throw new IllegalArgumentException("Package not found");
        }
        packageRepository.deleteById(id);
    }
}
