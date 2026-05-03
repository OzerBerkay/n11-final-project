package com.berkayozer.order.service.dataaccess.outbox.mapper;

import com.berkayozer.order.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.berkayozer.order.service.domain.outbox.model.OrderOutboxMessage;
import org.springframework.stereotype.Component;

@Component
public class OrderOutboxDataAccessMapper {

    public OrderOutboxMessage orderOutboxEntityToOrderOutboxMessage(OrderOutboxEntity entity) {
        return OrderOutboxMessage.builder()
                .id(entity.getId())
                .aggregateId(entity.getAggregateId())
                .type(entity.getType())
                .payload(entity.getPayload())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public OrderOutboxEntity orderOutboxMessageToOrderOutboxEntity(OrderOutboxMessage message) {
        return OrderOutboxEntity.builder()
                .id(message.getId())
                .aggregateId(message.getAggregateId())
                .type(message.getType())
                .payload(message.getPayload())
                .status(message.getStatus())
                .createdAt(message.getCreatedAt())
                .build();
    }
}