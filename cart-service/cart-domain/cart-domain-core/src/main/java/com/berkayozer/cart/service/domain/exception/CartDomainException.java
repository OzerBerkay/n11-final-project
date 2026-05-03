package com.berkayozer.cart.service.domain.exception;

import com.berkayozer.domain.exception.DomainException;

public class CartDomainException extends DomainException {
    public CartDomainException(String message) {
        super(message);
    }
    public CartDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}