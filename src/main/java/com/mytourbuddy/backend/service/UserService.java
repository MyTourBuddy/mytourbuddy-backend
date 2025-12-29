package com.mytourbuddy.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mytourbuddy.backend.dto.request.RegisterRequest;
import com.mytourbuddy.backend.dto.request.UpdateRequest;
import com.mytourbuddy.backend.dto.response.UserResponse;
import com.mytourbuddy.backend.mapper.UserMapper;
import com.mytourbuddy.backend.model.Role;
import com.mytourbuddy.backend.model.User;
import com.mytourbuddy.backend.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // get all users
    // public List<User> getAllUsers() {
    // return userRepository.findAll();
    // }
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).collect(Collectors.toList());
    }

    // get user by id
    // public Optional<User> getUserById(String id) {
    // return userRepository.findById(id);
    // }
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    // get user by username
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return userMapper.toResponse(user);
    }

    // create user
    // public User createUser(User user) {
    // if (userRepository.existsByUsername(user.getUsername())) {
    // throw new IllegalArgumentException("Username already exists");
    // }

    // if (userRepository.existsByEmail(user.getEmail())) {
    // throw new IllegalArgumentException("Email already exists");
    // }

    // if (!checkProfile(user)) {
    // throw new IllegalArgumentException("All fields are required and must not be
    // empty.");
    // }

    // user.setIsProfileComplete(false);
    // user.setMemberSince(Instant.now());

    // return userRepository.save(user);
    // }
    public UserResponse createUser(RegisterRequest request) {
        // check username and password already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // validate role specific fields
        validateRoleSpecificFields(request);

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setIsProfileComplete(false);
        user.setMemberSince(Instant.now());

        if (request.getRole() == Role.GUIDE) {
            user.setIsVerified(false);
        }

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    // update user details
    // public User updateUser(User updatedUser) {
    // User existingUser = userRepository.findById(updatedUser.getId())
    // .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // if (updatedUser.getRole() != null &&
    // !updatedUser.getRole().equals(existingUser.getRole())) {
    // throw new IllegalArgumentException("Cannot update role");
    // }
    // if (updatedUser.getUsername() != null &&
    // !updatedUser.getUsername().equals(existingUser.getUsername())) {
    // throw new IllegalArgumentException("Cannot update username");
    // }
    // if (updatedUser.getEmail() != null &&
    // !updatedUser.getEmail().equals(existingUser.getEmail())) {
    // throw new IllegalArgumentException("Cannot update email");
    // }

    // copyNonNullProperties(updatedUser, existingUser);

    // return userRepository.save(existingUser);
    // }
    public UserResponse updateUser(String id, UpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        checkUpdateAuthorization(existingUser);

        userMapper.updateEntityFromRequest(request, existingUser);

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toResponse(updatedUser);
    }

    // delete user by id
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // validate role specific fields
    private void validateRoleSpecificFields(RegisterRequest request) {
        if (request.getRole() == Role.TOURIST) {
            if (request.getCountry() == null || request.getCountry().isEmpty()) {
                throw new IllegalArgumentException("Country is required for tourists");
            }
            if (request.getTravelPreferences() == null || request.getTravelPreferences().isEmpty()) {
                throw new IllegalArgumentException("Travel preferences are required for tourists");
            }
        }

        if (request.getRole() == Role.GUIDE) {
            if (request.getLanguages() == null || request.getLanguages().isEmpty()) {
                throw new IllegalArgumentException("Languages are required for guides");
            }
            if (request.getYearsOfExp() == null || request.getYearsOfExp() < 0) {
                throw new IllegalArgumentException("Years of experience is required for guides");
            }
        }
    }

    // allow update user if admin or logged user
    private void checkUpdateAuthorization(User userToUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !userToUpdate.getUsername().equals(currentUsername)) {
            throw new SecurityException("You are not authorized to update this profile");
        }
    }

    // copy non null properties from source(data from frontend) to target
    // private void copyNonNullProperties(Object source, Object target) {
    // BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    // }

    // private String[] getNullPropertyNames(Object source) {
    // final BeanWrapper src = new BeanWrapperImpl(source);
    // PropertyDescriptor[] pds = src.getPropertyDescriptors();

    // Set<String> emptyNames = new HashSet<>();
    // for (PropertyDescriptor pd : pds) {
    // Object srcValue = src.getPropertyValue(pd.getName());
    // if (srcValue == null)
    // emptyNames.add(pd.getName());
    // }

    // emptyNames.add("id");
    // emptyNames.add("role");
    // emptyNames.add("username");
    // emptyNames.add("email");
    // emptyNames.add("memberSince");

    // return emptyNames.toArray(new String[0]);
    // }

    // verify user before create
    // private boolean checkProfile(User user) {
    // // common
    // if (user.getFirstName() == null || user.getFirstName().isEmpty())
    // return false;
    // if (user.getLastName() == null || user.getLastName().isEmpty())
    // return false;
    // if (user.getEmail() == null || user.getEmail().isEmpty())
    // return false;
    // if (user.getAge() == null || user.getAge() < 1 || user.getAge() > 150)
    // return false;
    // if (user.getUsername() == null || user.getUsername().isEmpty())
    // return false;
    // if (user.getPassword() == null || user.getPassword().isEmpty())
    // return false;

    // if (user.getRole() == Role.TOURIST) {
    // return user.getCountry() != null && !user.getCountry().isEmpty()
    // && user.getTravelPreferences() != null
    // && !user.getTravelPreferences().isEmpty();
    // }

    // if (user.getRole() == Role.GUIDE) {
    // return user.getLanguages() != null
    // && !user.getLanguages().isEmpty()
    // && user.getYearsOfExp() != null
    // && user.getYearsOfExp() >= 0;
    // }

    // return user.getRole() == Role.ADMIN;
    // }

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
