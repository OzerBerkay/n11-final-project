package com.berkayozer.cart.service.application.exception.handler;

import com.berkayozer.application.handler.ErrorDTO;
import com.berkayozer.application.handler.GlobalExceptionHandler;
import com.berkayozer.cart.service.domain.exception.CartDomainException;
import com.berkayozer.cart.service.domain.exception.CartNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class CartGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {CartDomainException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(CartDomainException cartDomainException) {
        log.error(cartDomainException.getMessage(), cartDomainException);
        return ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(cartDomainException.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {CartNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleException(CartNotFoundException cartNotFoundException) {
        log.error(cartNotFoundException.getMessage(), cartNotFoundException);
        return ErrorDTO.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(cartNotFoundException.getMessage())
                .build();
    }
}