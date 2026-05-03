package com.berkayozer.product.service.domain.handler;

import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.product.service.domain.dto.message.ReleaseStockCommand;
import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.exception.ProductApplicationServiceException;
import com.berkayozer.product.service.domain.outbox.scheduler.ProductOutboxHelper;
import com.berkayozer.product.service.domain.ports.output.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class ProductStockReleaseCommandHandler {

    private final ProductRepository productRepository;
    private final ProductOutboxHelper productOutboxHelper;

    public ProductStockReleaseCommandHandler(ProductRepository productRepository, ProductOutboxHelper productOutboxHelper) {
        this.productRepository = productRepository;
        this.productOutboxHelper = productOutboxHelper;
    }

    @Transactional
    public void releaseStock(ReleaseStockCommand command) {
        UUID sagaId = UUID.fromString(command.getSagaId());

        // IDEMPOTENCY BARRIER: Has this cancellation operation (STOCK_RELEASED) been processed before?
        if (productOutboxHelper.isOutboxMessageProcessed(sagaId, "STOCK_RELEASED")) {
            log.warn("Duplicate ReleaseStockCommand received! Saga ID: {} has already been processed. Ignoring.", command.getSagaId());
            return;
        }

        Product product = productRepository.findById(new ProductId(command.getProductId()))
                .orElseThrow(() -> new ProductApplicationServiceException("Product not found with id: " + command.getProductId()));

        // The domain method is called and the stock is added back
        product.increaseStock(command.getQuantity());
        productRepository.save(product);

        log.info("Stock released (rollback successful) for product: {} via Order: {}", command.getProductId(), command.getOrderId());

        // Once the process is complete, save it to Outbox (both to provide an idempotency barrier and to throw it to Kafka if needed)
        productOutboxHelper.saveOrderOutboxMessage(command, "STOCK_RELEASED", sagaId);
    }
}