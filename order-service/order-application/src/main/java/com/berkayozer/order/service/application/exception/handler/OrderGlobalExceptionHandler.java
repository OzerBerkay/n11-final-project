package com.berkayozer.order.service.application.exception.handler;

import com.berkayozer.order.service.domain.exception.OrderDomainException;
import com.berkayozer.order.service.domain.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class OrderGlobalExceptionHandler {

    // Domain kuralları ihlal edildiğinde (Örn: Sepet boş, Fiyat uyuşmazlığı) -> 400 BAD REQUEST
    @ExceptionHandler(OrderDomainException.class)
    public ResponseEntity<String> handleOrderDomainException(OrderDomainException exception) {
        log.error("Order domain exception: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    // Sipariş bulunamadığında (Track Order) -> 404 NOT FOUND
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException exception) {
        log.error("Order not found exception: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}