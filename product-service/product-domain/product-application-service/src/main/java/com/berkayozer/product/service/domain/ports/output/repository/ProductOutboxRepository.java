package com.berkayozer.product.service.domain.ports.output.repository;

import com.berkayozer.outbox.OutboxStatus;
import com.berkayozer.product.service.domain.outbox.model.ProductOutboxMessage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductOutboxRepository {
    ProductOutboxMessage save(ProductOutboxMessage outboxMessage);
    List<ProductOutboxMessage> findByOutboxStatus(OutboxStatus outboxStatus);
    void deleteByOutboxStatus(OutboxStatus outboxStatus);
    Optional<ProductOutboxMessage> findBySagaIdAndType(UUID sagaId, String type);
}