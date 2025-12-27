package com.ute.foodiedash.domain.user.model;

import com.ute.foodiedash.domain.common.exception.BadRequestException;
import com.ute.foodiedash.domain.common.model.BaseEntity;
import com.ute.foodiedash.domain.user.enums.Gender;
import com.ute.foodiedash.domain.user.enums.RoleName;
import com.ute.foodiedash.domain.user.enums.UserStatus;
import com.ute.foodiedash.domain.user.enums.VehicleType;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class User extends BaseEntity {
    private Long id;
    private String email;
    private String phone;
    private String password;
    private String fullName;
    private String avatarUrl;
    private UserStatus status;

    private CustomerProfile customerProfile;
    private MerchantProfile merchantProfile;
    private DriverProfile driverProfile;
    private List<CustomerAddress> addresses = new ArrayList<>();
    private List<UserRole> roles = new ArrayList<>();

    private User() {}

    public static User reconstruct(
            Long id,
            String email,
            String phone,
            String password,
            String fullName,
            String avatarUrl,
            UserStatus status,
            CustomerProfile customerProfile,
            MerchantProfile merchantProfile,
            DriverProfile driverProfile,
            List<CustomerAddress> addresses,
            List<UserRole> roles,
            Instant createdAt,
            Instant updatedAt,
            String createdBy,
            String updatedBy,
            Instant deletedAt,
            Long version
    ) {
        User user = new User();
        user.id = id;
        user.email = email;
        user.phone = phone;
        user.password = password;
        user.fullName = fullName;
        user.avatarUrl = avatarUrl;
        user.status = status;
        user.customerProfile = customerProfile;
        user.merchantProfile = merchantProfile;
        user.driverProfile = driverProfile;
        user.addresses = addresses != null ? addresses : new ArrayList<>();
        user.roles = roles != null ? roles : new ArrayList<>();
        user.restoreAudit(createdAt, updatedAt, createdBy, updatedBy, deletedAt, version);
        return user;
    }

    public static User createCustomer(
            String email,
            String phone,
            String password,
            String fullName,
            Instant dateOfBirth,
            Gender gender
    ) {
        validateEmail(email);
        validatePassword(password);
        validateFullName(fullName);

        User user = new User();
        user.email = email;
        user.phone = phone;
        user.password = password;
        user.fullName = fullName;
        user.status = UserStatus.PENDING_VERIFICATION;
        user.customerProfile = CustomerProfile.create(null, dateOfBirth, gender);
        user.roles.add(UserRole.create(RoleName.CUSTOMER));
        user.addresses = new ArrayList<>();

        return user;
    }

    public static User createMerchant(
            String email,
            String phone,
            String password,
            String fullName,
            String businessName,
            String contactEmail,
            String contactPhone
    ) {
        validateEmail(email);
        validatePassword(password);
        validateFullName(fullName);

        User user = new User();
        user.email = email;
        user.phone = phone;
        user.password = password;
        user.fullName = fullName;
        user.status = UserStatus.PENDING_VERIFICATION;
        user.merchantProfile = MerchantProfile.create(
                user.id, businessName, contactEmail, contactPhone
        );
        user.roles.add(UserRole.create(RoleName.MERCHANT));
        user.addresses = new ArrayList<>();

        return user;
    }

    public static User createDriver(
            String email,
            String phone,
            String password,
            String fullName,
            VehicleType vehicleType
    ) {
        validateEmail(email);
        validatePassword(password);
        validateFullName(fullName);

        User user = new User();
        user.email = email;
        user.phone = phone;
        user.password = password;
        user.fullName = fullName;
        user.status = UserStatus.PENDING_VERIFICATION;

        user.driverProfile = DriverProfile.create(user.id, vehicleType);
        user.roles.add(UserRole.create(RoleName.DRIVER));
        user.addresses = new ArrayList<>();

        return user;
    }

    public void updateProfile(String fullName, String phone, String avatarUrl) {
        ensureActive();

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

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }

        this.password = newPassword;
    }

    public void verify() {
        if (this.status != UserStatus.PENDING_VERIFICATION) {
            throw new BadRequestException("User is already verified");
        }

        this.status = UserStatus.ACTIVE;
    }

    public void activate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new BadRequestException("User is already active");
        }
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new BadRequestException("User is already inactive");
        }
        this.status = UserStatus.INACTIVE;
    }

    public void assignRole(RoleName roleName) {
        ensureActive();
        if (hasRole(roleName)) {
            throw new BadRequestException("Role is already assigned to this user");
        }
        if (roleName == RoleName.CUSTOMER && hasRole(RoleName.MERCHANT)) {
            throw new BadRequestException("User cannot have both customer and merchant roles");
        }
        if (roleName == RoleName.MERCHANT && hasRole(RoleName.CUSTOMER)) {
            throw new BadRequestException("User cannot have both customer and merchant roles");
        }
        this.roles.add(UserRole.create(roleName));
    }

    public void removeRole(RoleName roleName) {
        ensureActive();
        if (!hasRole(roleName)) {
            throw new BadRequestException("Role is not assigned to this user");
        }
        if (this.roles.size() == 1) {
            throw new BadRequestException("Cannot remove the last role");
        }
        this.roles.removeIf(r -> r.getRoleName().equals(roleName));
    }

    public void addCustomerAddress(CustomerAddress address) {
        ensureActive();

        if (this.customerProfile == null) {
            throw new BadRequestException("User is not a customer");
        }

        address.attachToUser(this.id);

        if (address.isDefaultAddress() || this.addresses.isEmpty()) {
            this.addresses.forEach(CustomerAddress::unsetDefault);
            address.setAsDefault();
        }

        this.addresses.add(address);
    }

    public void updateCustomerAddress(Long addressId, CustomerAddress updatedAddress) {
        ensureActive();
        CustomerAddress existing = findAddressById(addressId)
                .orElseThrow(() -> new BadRequestException("Address not found"));

        existing.update(updatedAddress);

        if (updatedAddress.isDefaultAddress()) {
            this.addresses.stream()
                    .filter(addr -> !addr.getId().equals(addressId))
                    .forEach(CustomerAddress::unsetDefault);
        }
    }

    public void removeCustomerAddress(Long addressId) {
        ensureActive();

        CustomerAddress address = findAddressById(addressId)
                .orElseThrow(() -> new BadRequestException("Address not found"));

        if (address.isDefaultAddress() && this.addresses.size() > 1) {
            throw new BadRequestException("Cannot remove default address when there are other addresses");
        }

        this.addresses.remove(address);
    }

    public void setDefaultAddress(Long addressId) {
        ensureActive();

        CustomerAddress address = findAddressById(addressId)
                .orElseThrow(() -> new BadRequestException("Address not found"));

        this.addresses.forEach(CustomerAddress::unsetDefault);
        address.setAsDefault();
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public void ensureActive() {
        if (!isActive()) {
            throw new BadRequestException("User is not active");
        }
    }

    public boolean hasRole(RoleName roleName) {
        return this.roles.stream().anyMatch(
                r -> r.getRoleName().equals(roleName)
        );
    }

    public List<RoleName> getRoleNames() {
        return this.roles.stream()
                .map(UserRole::getRoleName)
                .collect(Collectors.toList());
    }

    private Optional<CustomerAddress> findAddressById(Long addressId) {
        return this.addresses.stream()
                .filter(ca -> Objects.equals(ca.getId(), addressId))
                .findFirst();
    }

    public boolean isCustomer() {
        return this.customerProfile != null;
    }

    public boolean isMerchant() {
        return this.merchantProfile != null;
    }

    public boolean isDriver() {
        return this.driverProfile != null;
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Email is required");
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("Password is required");
        }
    }

    private static void validateFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new BadRequestException("Full name is required");
        }
    }
}
