package com.berkayozer.user.service.domain.exception;

import com.berkayozer.domain.exception.DomainException;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String message) {
        super(message);
    }
}