package com.berkayozer.product.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseStockCommand {
    private String sagaId;
    private UUID orderId;
    private UUID productId;
    private int quantity;
}