package com.berkayozer.product.service.domain.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProductListResponse {
    private final List<ProductSummary> products;
    private final int currentPage;
    private final int totalPages;
    private final long totalItems;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProductSummary {
        private final String productId;
        private final String name;
        private final String price;
        private final String imageUrl;
        private final boolean active;
        private final boolean inStock;
    }
}