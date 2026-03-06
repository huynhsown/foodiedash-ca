package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.user.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CustomerProfile extends BaseEntity {

    private Long id;
    private Long userId;
    private Instant dateOfBirth;
    private Gender gender;

    public static CustomerProfile create(Long userId, Instant dateOfBirth, Gender gender) {
        if (userId == null) {
            throw new BadRequestException("USER_ID_REQUIRED");
        }

        CustomerProfile profile = new CustomerProfile();
        profile.userId = userId;
        profile.dateOfBirth = dateOfBirth;
        profile.gender = gender;

        return profile;
    }

    public void updateProfile(Instant dateOfBirth, Gender gender) {
        if (dateOfBirth != null) {
            this.dateOfBirth = dateOfBirth;
        }

        this.gender = gender;
    }
}
