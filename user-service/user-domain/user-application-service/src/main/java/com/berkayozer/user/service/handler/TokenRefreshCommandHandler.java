package com.berkayozer.user.service.handler;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.dto.login.LoginResponse;
import com.berkayozer.user.service.dto.login.TokenRefreshCommand;
import com.berkayozer.user.service.ports.output.repository.IdentityProviderPort;
import com.berkayozer.user.service.ports.output.repository.TokenRepository;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenRefreshCommandHandler {

    private final IdentityProviderPort identityProviderPort;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Transactional(readOnly = true)
    public LoginResponse refresh(TokenRefreshCommand command) {
        String refreshToken = command.getRefreshToken();

        // Is the token valid? (Format and expiration check)
        if (!identityProviderPort.validateToken(refreshToken)) {
            throw new UserDomainException("Invalid or expired refresh token. Please log in again.");
        }

        // Extract the email address and issue date (Issued At) from the token
        String email = identityProviderPort.getEmailFromToken(refreshToken);
        Date issueDate = identityProviderPort.getIssueDateFromToken(refreshToken);

        // Blacklist check (Configuration: Was the token generated before the cancellation date?)
        if (tokenRepository.isTokenRevokedForUser(email, issueDate)) {
            throw new UserDomainException("Your session has been revoked. Please log in again.");
        }

        // Find the user from the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDomainException("User not found!"));

        if (user.getAccountStatus().name().equals("BANNED")) {
            throw new UserDomainException("Your account has been banned!");
        }

        // Create new tokens
        String newAccessToken = identityProviderPort.generateAccessToken(user);
        String newRefreshToken = identityProviderPort.generateRefreshToken(user);

        log.info("Tokens refreshed successfully for user: {}", email);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}