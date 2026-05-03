package com.berkayozer.product.service.dataaccess.outbox.mapper;

import com.berkayozer.product.service.dataaccess.outbox.entity.ProductOutboxEntity;
import com.berkayozer.product.service.domain.outbox.model.ProductOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class ProductOutboxDataAccessMapper {

    public ProductOutboxEntity productOutboxMessageToOutboxEntity(ProductOutboxMessage productOutboxMessage) {
        return ProductOutboxEntity.builder()
                .id(productOutboxMessage.getId())
                .sagaId(productOutboxMessage.getSagaId())
                .createdAt(productOutboxMessage.getCreatedAt())
                .processedAt(productOutboxMessage.getProcessedAt())
                .type(productOutboxMessage.getType())
                .payload(productOutboxMessage.getPayload())
                .outboxStatus(productOutboxMessage.getOutboxStatus())
                .version(productOutboxMessage.getVersion())
                .build();
    }

    public ProductOutboxMessage outboxEntityToProductOutboxMessage(ProductOutboxEntity productOutboxEntity) {
        return ProductOutboxMessage.builder()
                .id(productOutboxEntity.getId())
                .sagaId(productOutboxEntity.getSagaId())
                .createdAt(productOutboxEntity.getCreatedAt())
                .processedAt(productOutboxEntity.getProcessedAt())
                .type(productOutboxEntity.getType())
                .payload(productOutboxEntity.getPayload())
                .outboxStatus(productOutboxEntity.getOutboxStatus())
                .version(productOutboxEntity.getVersion())
                .build();
    }
}