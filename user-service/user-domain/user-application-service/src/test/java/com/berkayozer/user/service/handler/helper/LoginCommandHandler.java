package com.berkayozer.user.service.handler;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.domain.valueobject.AccountStatus;
import com.berkayozer.user.service.dto.login.LoginCommand;
import com.berkayozer.user.service.dto.login.LoginResponse;
import com.berkayozer.user.service.ports.output.repository.IdentityProviderPort;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginCommandHandlerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private IdentityProviderPort identityProviderPort;

    @InjectMocks
    private LoginCommandHandler loginCommandHandler;

    @Test
    void shouldThrowException_whenUserNotFound() {
        // Arrange
        LoginCommand command = LoginCommand.builder().email("unknown@test.com").build();
        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        UserDomainException exception = assertThrows(UserDomainException.class, () -> loginCommandHandler.login(command));
        assertEquals("Invalid email or password!", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenPasswordIsIncorrect() {
        // Arrange
        LoginCommand command = LoginCommand.builder().email("user@test.com").password("wrongPass").build();
        User dummyUser = User.Builder.builder().password("hashedPass").build();

        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.of(dummyUser));
        when(identityProviderPort.checkPassword("wrongPass", "hashedPass")).thenReturn(false); // Şifreler eşleşmedi!

        // Act & Assert
        UserDomainException exception = assertThrows(UserDomainException.class, () -> loginCommandHandler.login(command));
        assertEquals("Invalid email or password!", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenUserIsBanned() {
        // Arrange
        LoginCommand command = LoginCommand.builder().email("banned@test.com").password("correctPass").build();
        User bannedUser = User.Builder.builder()
                .password("hashedPass")
                .accountStatus(AccountStatus.BANNED) // DİKKAT: Kullanıcı BANLI!
                .build();

        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.of(bannedUser));
        when(identityProviderPort.checkPassword("correctPass", "hashedPass")).thenReturn(true); // Şifre doğru

        // Act & Assert
        UserDomainException exception = assertThrows(UserDomainException.class, () -> loginCommandHandler.login(command));
        assertEquals("Your account has been banned!", exception.getMessage());
    }

    @Test
    void shouldReturnTokens_whenLoginIsSuccessful() {
        // Arrange
        LoginCommand command = LoginCommand.builder().email("good@test.com").password("correctPass").build();
        User activeUser = User.Builder.builder()
                .password("hashedPass")
                .accountStatus(AccountStatus.ACTIVE) // Aktif kullanıcı
                .build();

        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.of(activeUser));
        when(identityProviderPort.checkPassword("correctPass", "hashedPass")).thenReturn(true);
        when(identityProviderPort.generateAccessToken(activeUser)).thenReturn("dummy-access-token");
        when(identityProviderPort.generateRefreshToken(activeUser)).thenReturn("dummy-refresh-token");

        // Act
        LoginResponse response = loginCommandHandler.login(command);

        // Assert
        assertNotNull(response);
        assertEquals("dummy-access-token", response.getAccessToken());
        assertEquals("dummy-refresh-token", response.getRefreshToken());
    }
}