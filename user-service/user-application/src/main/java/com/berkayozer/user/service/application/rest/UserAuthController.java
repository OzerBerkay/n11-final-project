package com.berkayozer.user.service.application.rest;

import com.berkayozer.user.service.dto.create.CreateUserCommand;
import com.berkayozer.user.service.dto.create.CreateUserResponse;
import com.berkayozer.user.service.dto.login.LoginCommand;
import com.berkayozer.user.service.dto.login.LoginResponse;
import com.berkayozer.user.service.dto.login.TokenRefreshCommand;
import com.berkayozer.user.service.ports.input.service.UserApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users", produces = "application/vnd.api.v1+json")
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "User Registration, Login, Token Refresh and Logout operations.")
public class UserAuthController {

    private final UserApplicationService userApplicationService;

    @Operation(summary = "Register a new user", description = "Creates a new user account with default ROLE_USER. Returns the generated User ID.")
    @SecurityRequirements // This endpoint removes the lock because it doesn't want JWT
    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> register(@RequestBody @Valid CreateUserCommand command) {
        log.info("Received register user request for email: {}", command.getEmail());
        CreateUserResponse response = userApplicationService.registerUser(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Login to the system", description = "Authenticates the user and returns an Access Token and a Refresh Token.")
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginCommand command) {
        log.info("Received login request for email: {}", command.getEmail());
        LoginResponse response = userApplicationService.login(command);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh JWT Tokens", description = "Generates new Access and Refresh tokens using a valid Refresh Token.")
    @SecurityRequirements
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody @Valid TokenRefreshCommand command) {
        log.info("Received token refresh request");
        LoginResponse response = userApplicationService.refresh(command);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout user", description = "Revokes all active refresh tokens for the authenticated user. Requires Bearer Token.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Subject (email) returned from JWT
        log.info("Received logout request for email: {}", email);
        userApplicationService.logout(email);
        return ResponseEntity.ok("Successfully logged out. Tokens have been revoked.");
    }
}