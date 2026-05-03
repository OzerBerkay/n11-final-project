package com.berkayozer.product.service.domain.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ProductDetailResponse {
    private final UUID productId;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Integer stockQuantity;
    private final UUID categoryId;
    private final String brand;
    private final String model;
    private final String color;
    private final String imageUrl;
    private final Boolean active;
}