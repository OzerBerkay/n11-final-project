package com.berkayozer.user.service.domain.entity;

import com.berkayozer.domain.entity.AggregateRoot;
import com.berkayozer.domain.valueobject.UserId;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.domain.valueobject.AccountStatus;
import com.berkayozer.user.service.domain.valueobject.FirstName;
import com.berkayozer.user.service.domain.valueobject.LastName;

import java.util.Set;
import java.util.UUID;

public class User extends AggregateRoot<UserId> {
    private final FirstName firstName;
    private final LastName lastName;
    private final String email;
    private String password;  // Updatable (Password reset => Gmail API for sending emails)
    private AccountStatus accountStatus; // For Banning/Activation
    private Address address; //Address can be added later but needed for order
    private final Set<Role> roles;

    public void initializeUser() {
        setId(new UserId(UUID.randomUUID()));
        this.accountStatus = AccountStatus.ACTIVE; // Directly active because there will be no mail validation
    }

    public void updateAddress(Address newAddress) {
        this.address = newAddress;
    }

    // Punishment by the admin
    public void banAccount() {
        this.accountStatus = AccountStatus.BANNED;
    }

    // The user freezing their own account.
    public void deactivateAccount() {
        this.accountStatus = AccountStatus.INACTIVE;
    }

    // Account reactivation (removing the ban or canceling the suspension)
    public void activateAccount() {
        if (this.accountStatus == AccountStatus.ACTIVE) {
            throw new UserDomainException("Account is already active!");
        }
        this.accountStatus = AccountStatus.ACTIVE;
    }

    private User(Builder builder) {
        super.setId(builder.userId);
        firstName = builder.firstName;
        lastName = builder.lastName;
        email = builder.email;
        password = builder.password;
        accountStatus = builder.accountStatus;
        address = builder.address;
        roles = builder.roles;
    }

    // Getters
    public FirstName getFirstName() { return firstName; }
    public LastName getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public AccountStatus getAccountStatus() { return accountStatus; }
    public Address getAddress() { return address; }
    public Set<Role> getRoles() { return roles; }

    // Builder (Kodu temiz tutmak için)
    public static final class Builder {
        private UserId userId;
        private FirstName firstName;
        private LastName lastName;
        private String email;
        private String password;
        private AccountStatus accountStatus;
        private Address address;
        private Set<Role> roles;

        private Builder() {}

        public static Builder builder() { return new Builder(); }

        public Builder id(UserId val) { userId = val; return this; }
        public Builder firstName(FirstName val) { firstName = val; return this; }
        public Builder lastName(LastName val) { lastName = val; return this; }
        public Builder email(String val) { email = val; return this; }
        public Builder password(String val) { password = val; return this; }
        public Builder accountStatus(AccountStatus val) { accountStatus = val; return this; }
        public Builder address(Address val) { address = val; return this; }
        public Builder roles(Set<Role> val) { roles = val; return this; }

        public User build() { return new User(this); }
    }
}