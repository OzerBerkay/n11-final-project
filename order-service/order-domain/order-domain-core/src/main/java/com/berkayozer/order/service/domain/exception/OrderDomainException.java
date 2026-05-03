package com.berkayozer.order.service.domain.exception;
import com.berkayozer.domain.exception.DomainException;

public class OrderDomainException extends DomainException {
    public OrderDomainException(String message) { super(message); }
    public OrderDomainException(String message, Throwable cause) { super(message, cause); }
}