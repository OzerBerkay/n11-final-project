package com.berkayozer.product.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ReserveStockCommand {
    private final String sagaId; // Critical for idempotency
    private final UUID orderId;
    private final UUID productId;
    private final int quantity; // Amount of stock to be returned
}