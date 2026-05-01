package com.berkayozer.user.service.application.security;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.domain.valueobject.UserRole;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service("roleAuthService") // We will call it by this name in @PreAuthorize.
@RequiredArgsConstructor
public class RoleAuthorizationService { // Prevents privilege escalation attacks

    private final UserRepository userRepository;

    /**
     * Checks if the user performing the action has the necessary permissions on the target user.
     * Rule: ADMINs cannot interact with SUPER_ADMINs!
     */
    public boolean canManageUser(UUID targetUserId) {
        // Get current user's role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean isCurrentUserSuperAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(UserRole.ROLE_SUPER_ADMIN.name()));

        // If the person performing the action is the Super Admin, they can touch anyone. (Return to True)
        if (isCurrentUserSuperAdmin) {
            return true;
        }

        // Retrieve the Target User role from the database
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserDomainException("Target user not found!"));

        // Rule: If the person performing the action is not a Super Admin (i.e., a regular ADMIN) and the target is SUPER_ADMIN, REJECT!
        if (targetUser.getRole() == UserRole.ROLE_SUPER_ADMIN) {
            log.warn("Security Alert: User {} attempted to modify SUPER_ADMIN {}", authentication.getName(), targetUserId);
            return false; // No authority
        }

        return true; // All other situations (such as the Admin performing actions on a regular User) are permitted.
    }
}