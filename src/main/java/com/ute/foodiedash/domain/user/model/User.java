package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User extends BaseEntity {
    private Long id;
    private String email;
    private String phone;
    private String password;
    private String fullName;
    private String avatarUrl;
    private UserStatus status;

    public static User create(
            String email,
            String phone,
            String password,
            String fullName,
            String avatarUrl
    ) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("EMAIL_REQUIRED");
        }

        if (password == null || password.isBlank()) {
            throw new BadRequestException("PASSWORD_REQUIRED");
        }

        if (fullName == null || fullName.isBlank()) {
            throw new BadRequestException("FULL_NAME_REQUIRED");
        }

        User user = new User();
        user.email = email;
        user.phone = phone;
        user.password = password;
        user.fullName = fullName;
        user.avatarUrl = avatarUrl;
        user.status = UserStatus.PENDING_VERIFICATION;
        return user;
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new BadRequestException("INVALID_PASSWORD");
        }

        this.password = newPassword;
    }

    public void updateProfile(String fullName, String phone, String avatarUrl) {
        if (fullName != null && !fullName.isBlank()) {
            this.fullName = fullName;
        }

        if (phone != null && !phone.isBlank()) {
            this.phone = phone;
        }

        if (avatarUrl != null) {
            this.avatarUrl = avatarUrl;
        }
    }

    public void verify() {
        if (this.status != UserStatus.PENDING_VERIFICATION) {
            throw new BadRequestException("USER_ALREADY_VERIFIED");
        }

        this.status = UserStatus.ACTIVE;
    }

    public void activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new BadRequestException("USER_ALREADY_ACTIVE");
        }
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new BadRequestException("USER_ALREADY_INACTIVE");
        }
        this.status = UserStatus.INACTIVE;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public void ensureActive() {
        if (!isActive()) {
            throw new BadRequestException("USER_NOT_ACTIVE");
        }
    }
}
