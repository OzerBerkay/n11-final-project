package com.berkayozer.product.service.application.exception.handler;

import com.berkayozer.application.handler.ErrorDTO;
import com.berkayozer.application.handler.GlobalExceptionHandler;
import com.berkayozer.product.service.domain.exception.ProductApplicationServiceException;
import com.berkayozer.product.service.domain.exception.ProductDomainException;
import com.berkayozer.product.service.domain.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ProductGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {ProductDomainException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ProductDomainException productDomainException) {
        log.error(productDomainException.getMessage(), productDomainException);
        return ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(productDomainException.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {ProductApplicationServiceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ProductApplicationServiceException productApplicationServiceException) {
        log.error(productApplicationServiceException.getMessage(), productApplicationServiceException);
        return ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(productApplicationServiceException.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {ProductNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleException(ProductNotFoundException productNotFoundException) {
        log.error(productNotFoundException.getMessage(), productNotFoundException);
        return ErrorDTO.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(productNotFoundException.getMessage())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {OptimisticLockingFailureException.class})
    @ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict durum kodu daha uygundur
    public ErrorDTO handleOptimisticLockingException(OptimisticLockingFailureException ex) {
        log.error("Optimistic locking failure: {}", ex.getMessage());
        return ErrorDTO.builder()
                .code(HttpStatus.CONFLICT.getReasonPhrase())
                // Kullanıcıya teknik detay vermek yerine durumu anlatan kibar bir mesaj
                .message("Bu ürün şu anda başka bir işlem tarafından güncellenmektedir. Lütfen tekrar deneyin.")
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        return ErrorDTO.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("İşlem başarısız. Eklemeye çalıştığınız verinin bağlı olduğu bir kayıt (örn: Kategori) bulunamadı veya benzersizlik kuralı aşıldı.")
                .build();
    }
}