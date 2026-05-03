package com.berkayozer.product.service.domain.exception;

import com.berkayozer.domain.exception.DomainException;

public class ProductApplicationServiceException extends DomainException {
    public ProductApplicationServiceException(String message) {
        super(message);
    }

    public ProductApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}