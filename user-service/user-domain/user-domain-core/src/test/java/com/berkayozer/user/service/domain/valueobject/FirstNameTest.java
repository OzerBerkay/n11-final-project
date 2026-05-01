package com.berkayozer.user.service.domain.valueobject;

import com.berkayozer.user.service.domain.exception.UserDomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstNameTest {

    @Test
    void shouldCreateFirstName_whenValid() {
        FirstName firstName = new FirstName("Berkay");
        assertEquals("Berkay", firstName.getValue());
    }

    @Test
    void shouldThrowException_whenEmpty() {
        assertThrows(UserDomainException.class, () -> new FirstName(""));
    }

    @Test
    void shouldThrowException_whenTooLong() {
        String longName = "A".repeat(51); // 50'den büyük
        assertThrows(UserDomainException.class, () -> new FirstName(longName));
    }
}