package com.berkayozer.user.service.domain.entity;

import com.berkayozer.user.service.domain.valueobject.AccountStatus;
import com.berkayozer.user.service.domain.valueobject.Email;
import com.berkayozer.user.service.domain.valueobject.FirstName;
import com.berkayozer.user.service.domain.valueobject.LastName;
import com.berkayozer.user.service.domain.valueobject.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldInitializeUserCorrectly() {
        // Arrange
        User user = User.Builder.builder()
                .firstName(new FirstName("Berkay"))
                .lastName(new LastName("Ozer"))
                .email(new Email("berkay@test.com"))
                .build();

        // Act
        user.initializeUser();

        // Assert
        assertNotNull(user.getId());
        assertEquals(AccountStatus.ACTIVE, user.getAccountStatus());
        assertEquals(UserRole.ROLE_USER, user.getRole());
    }

    @Test
    void shouldUpdateAddress() {
        // Arrange
        User user = User.Builder.builder().build(); // Test için boş builder yeterli

        // Act
        user.updateAddress("Istanbul, Turkey");

        // Assert
        assertEquals("Istanbul, Turkey", user.getAddress());
    }

    @Test
    void shouldBanAccount() {
        // Arrange
        User user = User.Builder.builder().build();
        user.initializeUser(); // Önce aktif yap

        // Act
        user.banAccount();

        // Assert
        assertEquals(AccountStatus.BANNED, user.getAccountStatus());
    }
}