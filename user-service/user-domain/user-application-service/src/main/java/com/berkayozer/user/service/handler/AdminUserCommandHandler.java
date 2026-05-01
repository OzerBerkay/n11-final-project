package com.berkayozer.user.service.handler;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.domain.valueobject.UserRole;
import com.berkayozer.user.service.ports.output.repository.TokenRepository;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserCommandHandler {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Transactional
    public void banUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDomainException("User not found with id: " + userId));

        user.banAccount(); // Domain kuralı işletildi
        userRepository.save(user);

        // Kullanıcı banlandığı an aktif tokenlarını da patlatıyoruz ki içeride kalamasın
        tokenRepository.revokeAllTokensForUser(user.getEmail().getValue());
        log.info("Admin Action: User {} has been banned and tokens revoked.", userId);
    }

    public void revokeTokens(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDomainException("User not found with id: " + userId));

        tokenRepository.revokeAllTokensForUser(user.getEmail().getValue());
        log.info("Admin Action: Tokens revoked for user {}", userId);
    }

    @Transactional
    public void updateUserRole(UUID userId, String newRoleStr) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDomainException("User not found!"));

        try {
            UserRole newRole = UserRole.valueOf(newRoleStr);
            user.updateRole(newRole);
            userRepository.save(user);
            log.info("Admin Action: User {} role updated to {}", userId, newRole.name());
        } catch (IllegalArgumentException e) {
            throw new UserDomainException("Invalid role provided!");
        }
    }
}