package com.berkayozer.user.service.handler;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.dto.login.LoginCommand;
import com.berkayozer.user.service.dto.login.LoginResponse;
import com.berkayozer.user.service.ports.output.repository.IdentityProviderPort;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCommandHandler {

    private final UserRepository userRepository;
    private final IdentityProviderPort identityProviderPort;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginCommand command) {
        // Find the user
        User user = userRepository.findByEmail(command.getEmail())
                .orElseThrow(() -> new UserDomainException("Invalid email or password!")); // Güvenlik için spesifik hata vermiyoruz

        // Password control
        if (!identityProviderPort.checkPassword(command.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for email: {}", command.getEmail());
            throw new UserDomainException("Invalid email or password!");
        }

        // Checking if the account is BANNED
        if (user.getAccountStatus().name().equals("BANNED")) {
            throw new UserDomainException("Your account has been banned!");
        }

        // Generate tokens
        String accessToken = identityProviderPort.generateAccessToken(user);
        String refreshToken = identityProviderPort.generateRefreshToken(user);

        log.info("User logged in successfully: {}", user.getEmail());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}