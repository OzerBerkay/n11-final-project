package com.berkayozer.user.service.domain.valueobject;

import com.berkayozer.user.service.domain.exception.UserDomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void shouldCreateEmail_whenFormatIsValid() {
        // Arrange & Act
        Email email = new Email("berkay.ozer@example.com");

        // Assert
        assertNotNull(email);
        assertEquals("berkay.ozer@example.com", email.getValue());
    }

    @Test
    void shouldThrowException_whenEmailIsEmpty() {
        // Act & Assert
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new Email("   "));
        assertEquals("Email cannot be empty!", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenEmailFormatIsInvalid() {
        // Act & Assert
        UserDomainException exception = assertThrows(UserDomainException.class, () -> new Email("invalid-email.com"));
        assertEquals("Invalid email format!", exception.getMessage());
    }
}