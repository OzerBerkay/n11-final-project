package com.berkayozer.user.service.domain.valueobject;

import com.berkayozer.user.service.domain.exception.UserDomainException;
import java.util.Objects;
import java.util.regex.Pattern;

public class Email {
    private final String value;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new UserDomainException("Email cannot be empty!");
        }
        if (!Pattern.matches(EMAIL_PATTERN, value)) {
            throw new UserDomainException("Invalid email format!");
        }
        this.value = value;
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }
}