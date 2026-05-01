package com.berkayozer.user.service.handler.helper;

import com.berkayozer.domain.valueobject.UserId;
import com.berkayozer.user.service.domain.UserDomainService;
import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.dto.create.CreateUserCommand;
import com.berkayozer.user.service.mapper.UserDataMapper;
import com.berkayozer.user.service.ports.output.repository.IdentityProviderPort;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCreateHelperTest {

    @Mock
    private UserDomainService userDomainService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private IdentityProviderPort identityProviderPort;
    @Mock
    private UserDataMapper userDataMapper;

    @InjectMocks
    private UserCreateHelper userCreateHelper;

    @Test
    void shouldThrowException_whenEmailAlreadyExists() {
        // Arrange (Hazırlık)
        CreateUserCommand command = CreateUserCommand.builder()
                .email("test@test.com")
                .build();

        // Mocking: userRepository.findByEmail çağrıldığında, DOLU bir Optional dön. (Yani bu email var de)
        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.of(User.Builder.builder().build()));

        // Act & Assert (Aksiyon ve Doğrulama)
        UserDomainException exception = assertThrows(UserDomainException.class, () -> {
            userCreateHelper.persistUser(command);
        });

        assertEquals("User with email test@test.com already exists!", exception.getMessage());

        // userRepository.save METODU ASLA ÇAĞRILMAMIŞ OLMALI!
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldSuccessfullyPersistUser_whenEmailIsUnique() {
        // Arrange
        CreateUserCommand command = CreateUserCommand.builder()
                .email("new@test.com")
                .password("rawPassword")
                // Builder'ın diğer alanlarını da doldurabilirsin (firstName, lastName vb.)
                .firstName("Test")
                .lastName("User")
                .build();

        // ÇÖZÜM BURADA: Artık bomboş bir user değil, içinde geçerli bir ID olan bir user dönüyoruz!
        User mappedUser = User.Builder.builder()
                .id(new UserId(UUID.randomUUID())) // NPE HATASINI ÇÖZEN SATIR
                .build();

        // Mocking kurgusu
        when(userRepository.findByEmail(command.getEmail())).thenReturn(Optional.empty());
        when(userDataMapper.createUserCommandToUser(command)).thenReturn(mappedUser);
        when(identityProviderPort.hashPassword("rawPassword")).thenReturn("hashedPassword");
        // user.hashPassword("hashedPassword") mappedUser içinde tetiklenecek
        when(userRepository.save(any(User.class))).thenReturn(mappedUser);

        // Act
        User savedUser = userCreateHelper.persistUser(command);

        // Assert
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId()); // ID'nin null olmadığını doğrula
        verify(userRepository, times(1)).save(any(User.class));
        verify(identityProviderPort, times(1)).hashPassword("rawPassword");
    }
}