package com.berkayozer.user.service;

import com.berkayozer.user.service.dto.create.CreateUserCommand;
import com.berkayozer.user.service.dto.create.CreateUserResponse;
import com.berkayozer.user.service.dto.login.LoginCommand;
import com.berkayozer.user.service.dto.login.LoginResponse;
import com.berkayozer.user.service.dto.login.TokenRefreshCommand;
import com.berkayozer.user.service.dto.profile.GetUserResponse;
import com.berkayozer.user.service.dto.profile.UpdateAddressCommand;
import com.berkayozer.user.service.handler.*;
import com.berkayozer.user.service.ports.input.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class UserApplicationServiceImpl implements UserApplicationService {

    private final UserCreateCommandHandler userCreateCommandHandler;
    private final LoginCommandHandler loginCommandHandler;
    private final TokenRefreshCommandHandler tokenRefreshCommandHandler;
    private final LogoutCommandHandler logoutCommandHandler;
    private final AdminUserCommandHandler adminUserCommandHandler;
    private final GetUserProfileQueryHandler getUserProfileQueryHandler;
    private final UpdateAddressCommandHandler updateAddressCommandHandler;

    @Override
    public CreateUserResponse registerUser(CreateUserCommand createUserCommand) {
        return userCreateCommandHandler.createUser(createUserCommand);
    }

    @Override
    public LoginResponse login(LoginCommand loginCommand) {
        return loginCommandHandler.login(loginCommand);
    }

    @Override
    public LoginResponse refresh(TokenRefreshCommand tokenRefreshCommand) {
        return tokenRefreshCommandHandler.refresh(tokenRefreshCommand);
    }

    @Override
    public void logout(String email) {
        logoutCommandHandler.logout(email);
    }

    @Override
    public void revokeTokens(UUID userId) {
        adminUserCommandHandler.revokeTokens(userId);
    }

    @Override
    public void banUser(UUID userId) {
        adminUserCommandHandler.banUser(userId);
    }

    @Override
    public void updateUserRole(UUID userId, String newRole) {
        adminUserCommandHandler.updateUserRole(userId, newRole);
    }

    @Override
    public GetUserResponse getMyProfile(String email) {
        return getUserProfileQueryHandler.getUserProfile(email);
    }

    @Override
    public void updateMyAddress(String email, UpdateAddressCommand command) {
        updateAddressCommandHandler.updateAddress(email, command);
    }
}