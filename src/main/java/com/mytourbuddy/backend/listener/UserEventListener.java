package com.mytourbuddy.backend.listener;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.mytourbuddy.backend.model.Role;
import com.mytourbuddy.backend.model.User;

@Component
public class UserEventListener extends AbstractMongoEventListener<User> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<User> event) {
        User user = event.getSource();
        user.setIsProfileComplete(isProfileComplete(user));
    }

    private boolean isProfileComplete(User user) {
        if (user.getRole() == null) {
            return false;
        }

        if (user.getRole() == Role.ADMIN) {
            return true;
        }

        if (user.getRole() == Role.GUIDE) {
            return isNotEmpty(user.getFirstName()) &&
                    isNotEmpty(user.getLastName()) &&
                    isNotEmpty(user.getEmail()) &&
                    isNotEmpty(user.getUsername()) &&
                    user.getAge() != null &&
                    isNotEmpty(user.getPhone()) &&
                    isNotEmptyList(user.getLanguages()) &&
                    user.getYearsOfExp() != null &&
                    isNotEmptyList(user.getSpecializations()) &&
                    isNotEmpty(user.getEmergencyContact());
        } else if (user.getRole() == Role.TOURIST) {
            return isNotEmpty(user.getFirstName()) &&
                    isNotEmpty(user.getLastName()) &&
                    isNotEmpty(user.getEmail()) &&
                    isNotEmpty(user.getUsername()) &&
                    user.getAge() != null &&
                    isNotEmpty(user.getPhone()) &&
                    isNotEmptyList(user.getTravelPreferences()) &&
                    isNotEmpty(user.getCountry());
        }

        return false;
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isNotEmptyList(java.util.List<?> list) {
        return list != null && !list.isEmpty();
    }
}
