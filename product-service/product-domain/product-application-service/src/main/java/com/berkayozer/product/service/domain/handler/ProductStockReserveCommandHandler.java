package com.berkayozer.product.service.domain.handler;

import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.product.service.domain.dto.message.ReserveStockCommand;
import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.exception.ProductApplicationServiceException;
import com.berkayozer.product.service.domain.exception.ProductDomainException;
import com.berkayozer.product.service.domain.outbox.scheduler.ProductOutboxHelper;
import com.berkayozer.product.service.domain.ports.output.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class ProductStockReserveCommandHandler {

    private final ProductRepository productRepository;
    private final ProductOutboxHelper productOutboxHelper;

    public ProductStockReserveCommandHandler(ProductRepository productRepository,
                                             ProductOutboxHelper productOutboxHelper) {
        this.productRepository = productRepository;
        this.productOutboxHelper = productOutboxHelper;
    }

    @Transactional
    public void reserveStock(ReserveStockCommand command) {
        // IDEMPOTENCY CHECK IS DONE VIA OUTBOX.
        // (You can add an existsBySagaId method to the Helper, or you can catch the DataIntegrityViolationException that will be thrown during save if SagaId is unique in the Outbox table).
        if (productOutboxHelper.isOutboxMessageProcessed(UUID.fromString(command.getSagaId()), "STOCK_RESERVED")) {
            log.warn("Duplicate ReserveStockCommand received! Saga ID: {} has already been processed. Ignoring.", command.getSagaId());
            return;
        }

        try {
            Product product = productRepository.findById(new ProductId(command.getProductId()))
                    .orElseThrow(() -> new ProductApplicationServiceException("Product not found with id: " + command.getProductId()));

            product.decreaseStock(command.getQuantity());
            productRepository.save(product);

            log.info("Stock reserved for product: {} via Order: {}", command.getProductId(), command.getOrderId());
            productOutboxHelper.saveOrderOutboxMessage(command, "STOCK_RESERVED", UUID.fromString(command.getSagaId()));

        } catch (ProductDomainException | ProductApplicationServiceException e) {
            log.error("Stock reservation failed for order id: {}. Reason: {}", command.getOrderId(), e.getMessage());

            Map<String, Object> failurePayload = new HashMap<>();
            failurePayload.put("orderId", command.getOrderId());
            failurePayload.put("productId", command.getProductId());
            failurePayload.put("sagaId", command.getSagaId());
            failurePayload.put("failureMessages", e.getMessage());

            productOutboxHelper.saveOrderOutboxMessage(failurePayload, "STOCK_FAILED", UUID.fromString(command.getSagaId()));
        }
    }
}