package com.berkayozer.cart.service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CartResponse {
    private final UUID userId;
    private final BigDecimal totalAmount;
    private final List<CartItemResponse> items;
}