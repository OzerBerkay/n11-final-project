package com.berkayozer.product.service.domain.exception;

import com.berkayozer.domain.exception.DomainException;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}