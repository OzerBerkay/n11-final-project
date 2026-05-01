package com.berkayozer.user.service.application.rest;

import com.berkayozer.user.service.ports.input.service.UserApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users/admin", produces = "application/vnd.api.v1+json")
@RequiredArgsConstructor
@Tag(name = "Admin Operations API", description = "Administrative endpoints. Requires ROLE_ADMIN or ROLE_SUPER_ADMIN.")
public class AdminUserController {

    private final UserApplicationService userApplicationService;

    @Operation(summary = "Revoke User Tokens", description = "Revokes all active sessions for a specific user. Requires ADMIN or SUPER_ADMIN role.")
    @PostMapping("/revoke/{userId}")
    @PreAuthorize("(hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')) and @roleAuthService.canManageUser(#userId)")
    public ResponseEntity<String> revokeTokens(@PathVariable UUID userId) {
        log.info("Admin request: Revoking tokens for user: {}", userId);
        userApplicationService.revokeTokens(userId);
        return ResponseEntity.ok("Tokens for user " + userId + " have been successfully revoked.");
    }

    @Operation(summary = "Ban User", description = "Bans a specific user and revokes their active tokens. Requires ADMIN or SUPER_ADMIN role.")
    @PostMapping("/ban/{userId}")
    @PreAuthorize("(hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')) and @roleAuthService.canManageUser(#userId)")
    public ResponseEntity<String> banUser(@PathVariable UUID userId) {
        log.info("Admin request: Banning user: {}", userId);
        userApplicationService.banUser(userId);
        return ResponseEntity.ok("User " + userId + " has been successfully banned.");
    }

    @Operation(summary = "Update User Role (SUPER ADMIN ONLY)", description = "Changes the role of a user (e.g., from ROLE_USER to ROLE_ADMIN). Strictly requires ROLE_SUPER_ADMIN.")
    @PutMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')") // JUST SUPER ADMIN!
    public ResponseEntity<String> updateRole(@PathVariable UUID userId, @RequestParam String role) {
        log.info("Super Admin request: Updating role for user: {} to {}", userId, role);
        userApplicationService.updateUserRole(userId, role);
        return ResponseEntity.ok("User role updated successfully.");
    }
}