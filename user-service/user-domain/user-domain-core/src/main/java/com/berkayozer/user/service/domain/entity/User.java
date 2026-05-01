package com.berkayozer.user.service.domain.entity;

import com.berkayozer.domain.entity.AggregateRoot;
import com.berkayozer.domain.valueobject.UserId;
import com.berkayozer.user.service.domain.valueobject.*;

import java.util.UUID;

public class User extends AggregateRoot<UserId> {
    private final FirstName firstName;
    private final LastName lastName;
    private final Email email;
    private String password;  // Updatable (Password reset => Gmail API for sending emails)
    private AccountStatus accountStatus; // For Banning/Activation
    private String address; //Address can be added later but needed for order
    private UserRole role;

    public void initializeUser() {
        setId(new UserId(UUID.randomUUID()));
        this.accountStatus = AccountStatus.ACTIVE; // Directly active because there will be no mail validation
        this.role = UserRole.ROLE_USER; // Every user is just simple user at register
    }

    public void updateRole(UserRole newRole) {
        this.role = newRole;
    }

    public void hashPassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public void updateAddress(String newAddress) {
        this.address = newAddress;
    }


    // Punishment by the admin
    public void banAccount() {
        this.accountStatus = AccountStatus.BANNED;
    }


    private User(Builder builder) {
        super.setId(builder.userId);
        firstName = builder.firstName;
        lastName = builder.lastName;
        email = builder.email;
        password = builder.password;
        accountStatus = builder.accountStatus;
        address = builder.address;
        role = builder.role;
    }

    // Getters
    public FirstName getFirstName() { return firstName; }
    public LastName getLastName() { return lastName; }
    public Email getEmail() { return email; }
    public String getPassword() { return password; }
    public AccountStatus getAccountStatus() { return accountStatus; }
    public String getAddress() { return address; }
    public UserRole getRole() { return role; }

    public static final class Builder {
        private UserId userId;
        private FirstName firstName;
        private LastName lastName;
        private Email email;
        private String password;
        private AccountStatus accountStatus;
        private String address;
        private UserRole role;

        private Builder() {}

        public static Builder builder() { return new Builder(); }

        public Builder id(UserId val) { userId = val; return this; }
        public Builder firstName(FirstName val) { firstName = val; return this; }
        public Builder lastName(LastName val) { lastName = val; return this; }
        public Builder email(Email val) { email = val; return this; }
        public Builder password(String val) { password = val; return this; }
        public Builder accountStatus(AccountStatus val) { accountStatus = val; return this; }
        public Builder address(String val) { address = val; return this; }
        public Builder role(UserRole val) { role = val; return this; }

        public User build() { return new User(this); }
    }
}