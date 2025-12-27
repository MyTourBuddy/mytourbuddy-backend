package com.mytourbuddy.backend.mapper;

import org.springframework.stereotype.Component;

import com.mytourbuddy.backend.dto.request.RegisterRequest;
import com.mytourbuddy.backend.dto.request.UpdateRequest;
import com.mytourbuddy.backend.dto.response.GuideResponse;
import com.mytourbuddy.backend.dto.response.TouristResponse;
import com.mytourbuddy.backend.dto.response.UserResponse;
import com.mytourbuddy.backend.model.Role;
import com.mytourbuddy.backend.model.User;

@Component
public class UserMapper {
    // for create user
    public User toEntity(RegisterRequest request) {
        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        // tourist only
        user.setCountry(request.getCountry());
        user.setTravelPreferences(request.getTravelPreferences());

        // guide only
        user.setLanguages(request.getLanguages());
        user.setYearsOfExp(request.getYearsOfExp());

        return user;
    }

    // to response user
    public UserResponse toResponse(User user) {
        System.out.println("Mapping user with role: " + user.getRole());

        if (user.getRole() == Role.GUIDE) {
            return toGuideResponse(user);
        } else if (user.getRole() == Role.TOURIST) {
            return toTouristResponse(user);
        } else {
            return toBaseResponse(user);
        }
    }

    private GuideResponse toGuideResponse(User user) {
        GuideResponse response = new GuideResponse();

        setCommonFields(response, user);

        response.setLanguages(user.getLanguages());
        response.setYearsOfExp(user.getYearsOfExp());
        response.setBio(user.getBio());
        response.setSpecializations(user.getSpecializations());
        response.setCertifications(user.getCertifications());
        response.setEmergencyContact(user.getEmergencyContact());
        response.setWebsite(user.getWebsite());
        response.setSocialMedia(user.getSocialMedia());
        response.setIsVerified(user.getIsVerified());

        return response;
    }

    private TouristResponse toTouristResponse(User user) {
        TouristResponse response = new TouristResponse();

        setCommonFields(response, user);

        response.setCountry(user.getCountry());
        response.setTravelPreferences(user.getTravelPreferences());
        response.setPreferredDestinations(user.getPreferredDestinations());
        response.setTravelInterests(user.getTravelInterests());
        response.setLanguageSpoken(user.getLanguageSpoken());

        return response;
    }

    private UserResponse toBaseResponse(User user) {
        UserResponse response = new UserResponse();
        setCommonFields(response, user);
        return response;
    }

    private void setCommonFields(UserResponse response, User user) {
        response.setId(user.getId());
        response.setRole(user.getRole());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setAge(user.getAge());
        response.setAvatar(user.getAvatar());
        response.setPhone(user.getPhone());
        response.setIsProfileComplete(user.getIsProfileComplete());
        response.setMemberSince(user.getMemberSince());
    }

    // public UserResponse toResponse(User user) {
    // UserResponse response = new UserResponse();

    // response.setId(user.getId());
    // response.setRole(user.getRole());

    // response.setFirstName(user.getFirstName());
    // response.setLastName(user.getLastName());
    // response.setEmail(user.getEmail());
    // response.setAge(user.getAge());
    // response.setUsername(user.getUsername());
    // response.setAvatar(user.getAvatar());
    // response.setPhone(user.getPhone());
    // response.setIsProfileComplete(user.getIsProfileComplete());
    // response.setMemberSince(user.getMemberSince());

    // // tourist only
    // response.setCountry(user.getCountry());
    // response.setTravelPreferences(user.getTravelPreferences());
    // response.setPreferredDestinations(user.getPreferredDestinations());
    // response.setTravelInterests(user.getTravelInterests());
    // response.setLanguageSpoken(user.getLanguageSpoken());

    // // guide only
    // response.setLanguages(user.getLanguages());
    // response.setYearsOfExp(user.getYearsOfExp());
    // response.setBio(user.getBio());
    // response.setSpecializations(user.getSpecializations());
    // response.setCertifications(user.getCertifications());
    // response.setEmergencyContact(user.getEmergencyContact());
    // response.setWebsite(user.getWebsite());
    // response.setSocialMedia(user.getSocialMedia());
    // response.setIsVerified(user.getIsVerified());

    // return response;
    // }

    // to update user
    public void updateEntityFromRequest(UpdateRequest request, User user) {
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getAge() != null) {
            user.setAge(request.getAge());
        }

        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        // tourist only
        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
        }

        if (request.getTravelPreferences() != null) {
            user.setTravelPreferences(request.getTravelPreferences());
        }

        if (request.getPreferredDestinations() != null) {
            user.setPreferredDestinations(request.getPreferredDestinations());
        }

        if (request.getTravelInterests() != null) {
            user.setTravelInterests(request.getTravelInterests());
        }

        if (request.getLanguageSpoken() != null) {
            user.setLanguageSpoken(request.getLanguageSpoken());
        }

        // guide only
        if (request.getLanguages() != null) {
            user.setLanguages(request.getLanguages());
        }

        if (request.getYearsOfExp() != null) {
            user.setYearsOfExp(request.getYearsOfExp());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        if (request.getSpecializations() != null) {
            user.setSpecializations(request.getSpecializations());
        }

        if (request.getCertifications() != null) {
            user.setCertifications(request.getCertifications());
        }

        if (request.getEmergencyContact() != null) {
            user.setEmergencyContact(request.getEmergencyContact());
        }

        if (request.getWebsite() != null) {
            user.setWebsite(request.getWebsite());
        }

        if (request.getSocialMedia() != null) {
            user.setSocialMedia(request.getSocialMedia());
        }
    }
}
