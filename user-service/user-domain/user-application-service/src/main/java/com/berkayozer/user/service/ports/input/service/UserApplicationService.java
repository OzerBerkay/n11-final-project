package com.berkayozer.user.service.ports.input.service;

import com.berkayozer.user.service.dto.create.CreateUserCommand;
import com.berkayozer.user.service.dto.create.CreateUserResponse;
import com.berkayozer.user.service.dto.login.LoginCommand;
import com.berkayozer.user.service.dto.login.LoginResponse;
import com.berkayozer.user.service.dto.login.TokenRefreshCommand;
import com.berkayozer.user.service.dto.profile.GetUserResponse;
import com.berkayozer.user.service.dto.profile.UpdateAddressCommand;
import jakarta.validation.Valid;

import java.util.UUID;

public interface UserApplicationService {
    // Auth
    CreateUserResponse registerUser(@Valid CreateUserCommand createUserCommand);
    LoginResponse login(@Valid LoginCommand loginCommand);
    LoginResponse refresh(@Valid TokenRefreshCommand tokenRefreshCommand);
    void logout(String email);

    // Admin
    void revokeTokens(UUID userId);
    void banUser(UUID userId);

    // Super Admin
    void updateUserRole(UUID userId, String newRole);

    // Profile
    GetUserResponse getMyProfile(String email);
    void updateMyAddress(String email, UpdateAddressCommand command);
}