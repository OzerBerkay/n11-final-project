package com.berkayozer.user.service.application.rest;

import com.berkayozer.user.service.dto.profile.GetUserResponse;
import com.berkayozer.user.service.dto.profile.UpdateAddressCommand;
import com.berkayozer.user.service.ports.input.service.UserApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users/profile", produces = "application/vnd.api.v1+json")
@RequiredArgsConstructor
@Tag(name = "User Profile API", description = "Operations for users to view and update their own profile data. Requires Bearer Token.")
public class UserProfileController {

    private final UserApplicationService userApplicationService;

    @Operation(summary = "Get my profile", description = "Fetches the profile details (including role and address) of the currently authenticated user.")
    @GetMapping
    public ResponseEntity<GetUserResponse> getMyProfile() {
        // Security: We're extracting the email address of the person who sent the request from the JWT token.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.info("Received get profile request for email: {}", email);
        return ResponseEntity.ok(userApplicationService.getMyProfile(email));
    }

    @Operation(summary = "Update my address", description = "Updates the physical address of the currently authenticated user.")
    @PutMapping("/address")
    public ResponseEntity<String> updateMyAddress(@RequestBody @Valid UpdateAddressCommand command) {
        // Security: We're extracting the email address of the person who sent the request from the JWT token.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.info("Received update address request for email: {}", email);
        userApplicationService.updateMyAddress(email, command);
        return ResponseEntity.ok("Address updated successfully.");
    }
}