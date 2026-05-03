package com.berkayozer.product.service.dataaccess.outbox.repository;

import com.berkayozer.outbox.OutboxStatus;
import com.berkayozer.product.service.dataaccess.outbox.entity.ProductOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductOutboxJpaRepository extends JpaRepository<ProductOutboxEntity, UUID> {
    Optional<List<ProductOutboxEntity>> findByOutboxStatus(OutboxStatus outboxStatus);
    void deleteByOutboxStatus(OutboxStatus outboxStatus);
    Optional<ProductOutboxEntity> findBySagaIdAndType(UUID sagaId, String type);
}