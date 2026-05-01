package com.berkayozer.user.service.domain;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.event.UserCreatedEvent;
import com.berkayozer.user.service.domain.valueobject.Email;
import com.berkayozer.user.service.domain.valueobject.FirstName;
import com.berkayozer.user.service.domain.valueobject.LastName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDomainServiceTest {

    private final UserDomainService userDomainService = new UserDomainServiceImpl();

    @Test
    void shouldValidateAndInitiateUserAndReturnEvent() {
        // Arrange
        User user = User.Builder.builder()
                .firstName(new FirstName("Jane"))
                .lastName(new LastName("Doe"))
                .email(new Email("jane.doe@example.com"))
                .build();

        // Act
        UserCreatedEvent event = userDomainService.validateAndInitiateUser(user);

        // Assert
        assertNotNull(event);
        assertNotNull(event.getCreatedAt()); // Event zamanı atandı mı?
        assertEquals(user, event.getUser()); // Event içindeki kullanıcı doğru mu?
        assertNotNull(user.getId()); // ID atandı mı?
    }
}