package com.berkayozer.product.service.domain.event;

import com.berkayozer.product.service.domain.entity.Product;

import java.time.ZonedDateTime;

public class ProductInformationEvent {
    private final Product product;
    private final ZonedDateTime createdAt;

    public ProductInformationEvent(Product product, ZonedDateTime createdAt) {
        this.product = product;
        this.createdAt = createdAt;
    }

    public Product getProduct() { return product; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
}
