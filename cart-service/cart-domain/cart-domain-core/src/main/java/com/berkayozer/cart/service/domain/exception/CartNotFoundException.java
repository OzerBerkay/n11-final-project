package com.berkayozer.cart.service.domain.exception;

import com.berkayozer.domain.exception.DomainException;

public class CartNotFoundException extends DomainException {
    public CartNotFoundException(String message) {
        super(message);
    }
}