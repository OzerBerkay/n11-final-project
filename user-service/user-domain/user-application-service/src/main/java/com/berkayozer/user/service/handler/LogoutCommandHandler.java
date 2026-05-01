package com.berkayozer.user.service.handler;

import com.berkayozer.user.service.ports.output.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutCommandHandler {

    private final TokenRepository tokenRepository;

    public void logout(String email) {
        tokenRepository.revokeAllTokensForUser(email);
        log.info("All tokens revoked (logged out) for email: {}", email);
    }
}