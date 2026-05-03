package com.berkayozer.order.service.domain.outbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class OrderOutboxMessage {
    private UUID id; // Outbox kaydının kendi ID'si
    private UUID aggregateId; // Order ID
    private String type; // Örn: "ORDER_CREATED"
    private String payload; // Event'in JSON hali
    private String status; // STARTED, COMPLETED, FAILED
    private ZonedDateTime createdAt;
}