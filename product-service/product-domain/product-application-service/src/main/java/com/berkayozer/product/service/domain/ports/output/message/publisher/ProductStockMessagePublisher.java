package com.berkayozer.product.service.domain.ports.output.message.publisher;

import com.berkayozer.outbox.OutboxStatus;
import com.berkayozer.product.service.domain.outbox.model.ProductOutboxMessage;

import java.util.function.BiConsumer;

public interface ProductStockMessagePublisher {

    void publish(ProductOutboxMessage outboxMessage,
                 BiConsumer<ProductOutboxMessage, OutboxStatus> outboxCallback);
}