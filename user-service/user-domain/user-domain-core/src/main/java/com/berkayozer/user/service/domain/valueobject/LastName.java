package com.berkayozer.user.service.domain.valueobject;

import com.berkayozer.user.service.domain.exception.UserDomainException;

import java.util.Objects;

public class LastName {
    private final String value;

    public LastName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new UserDomainException("Last name cannot be empty!");
        }
        if (value.length() > 50) {
            throw new UserDomainException("Last name cannot exceed 50 characters!");
        }
        this.value = value;
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastName lastName = (LastName) o;
        return Objects.equals(value, lastName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}