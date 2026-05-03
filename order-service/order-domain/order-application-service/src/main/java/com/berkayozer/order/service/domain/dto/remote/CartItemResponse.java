package com.berkayozer.order.service.domain.dto.remote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CartItemResponse {
    private final UUID productId;
    private final Integer quantity;
    private final BigDecimal price;
    private final BigDecimal subTotal;
}
