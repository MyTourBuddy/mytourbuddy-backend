package com.mytourbuddy.backend.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.model.Role;
import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.UserRepository;

import java.beans.PropertyDescriptor;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // get user by id
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // create user
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!checkProfile(user)) {
            throw new IllegalArgumentException("All fields are required and must not be empty.");
        }

        user.setIsProfileComplete(false);
        user.setMemberSince(Instant.now());

        return userRepository.save(user);
    }

    // update user details
    public User updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (updatedUser.getRole() != null && !updatedUser.getRole().equals(existingUser.getRole())) {
            throw new IllegalArgumentException("Cannot update role");
        }
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(existingUser.getUsername())) {
            throw new IllegalArgumentException("Cannot update username");
        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
            throw new IllegalArgumentException("Cannot update email");
        }

        copyNonNullProperties(updatedUser, existingUser);

        return userRepository.save(existingUser);
    }

    // delete user by id
    public void deleteUserById(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }


    // copy non null properties from source(data from frontend) to target
    private void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }

        emptyNames.add("id");
        emptyNames.add("role");
        emptyNames.add("username");
        emptyNames.add("email");
        emptyNames.add("memberSince");

        return emptyNames.toArray(new String[0]);
    }

    // verify user before create
    private boolean checkProfile(User user) {
        // common
        if (user.getFirstName() == null || user.getFirstName().isEmpty())
            return false;
        if (user.getLastName() == null || user.getLastName().isEmpty())
            return false;
        if (user.getEmail() == null || user.getEmail().isEmpty())
            return false;
        if (user.getAge() == null || user.getAge() < 1 || user.getAge() > 150)
            return false;
        if (user.getUsername() == null || user.getUsername().isEmpty())
            return false;
        if (user.getPassword() == null || user.getPassword().isEmpty())
            return false;

        if (user.getRole() == Role.TOURIST) {
            return user.getCountry() != null && !user.getCountry().isEmpty()
                    && user.getTravelPreferences() != null
                    && !user.getTravelPreferences().isEmpty();
        }

        if (user.getRole() == Role.GUIDE) {
            return user.getLanguages() != null
                    && !user.getLanguages().isEmpty()
                    && user.getYearsOfExp() != null
                    && user.getYearsOfExp() >= 0;
        }

        return user.getRole() == Role.ADMIN;
    }

    // todo - for profile completion bar
    // public boolean checkIsProfileComplete(User user) {
    // if (user.getFirstName() == null || user.getFirstName().isEmpty())
    // return false;
    // if (user.getLastName() == null || user.getLastName().isEmpty())
    // return false;
    // if (user.getEmail() == null || user.getEmail().isEmpty())
    // return false;
    // if (user.getUsername() == null || user.getUsername().isEmpty())
    // return false;
    // if (user.getPassword() == null || user.getPassword().isEmpty())
    // return false;
    // if (user.getAge() == null || user.getAge() < 1 || user.getAge() > 120)
    // return false;
    // if (user.getAvatar() == null || user.getAvatar().isEmpty())
    // return false;
    // if (user.getPhone() == null || user.getPhone().isEmpty())
    // return false;
    // if (user.getMemberSince() == null)
    // return false;
    // if (user.getRole() == null)
    // return false;

    // if (user.getRole() == Role.TOURIST) {
    // if (user.getCountry() == null || user.getCountry().isEmpty())
    // return false;
    // if (user.getTravelPreferences() == null ||
    // user.getTravelPreferences().isEmpty())
    // return false;
    // }

    // if (user.getRole() == Role.GUIDE) {
    // if (user.getLanguages() == null || user.getLanguages().isEmpty()) return
    // false;
    // if (user.getYearsOfExp() == null || user.getYearsOfExp() < 0) return false;
    // if (user.getSpecializations() == null || user.getSpecializations().isEmpty())
    // return false;
    // if (user.getDailyRate() == null || user.getDailyRate() < 0) return false;
    // if (user.getMaxGroupSize() == null || user.getMaxGroupSize() < 1) return
    // false;
    // if (user.getTransportMode() == null || user.getTransportMode().isEmpty())
    // return false;
    // if (user.getAgeGroups() == null || user.getAgeGroups().isEmpty()) return
    // false;
    // if (user.getWorkingDays() == null || user.getWorkingDays().isEmpty()) return
    // false;
    // }

    // return true;
    // }

}
