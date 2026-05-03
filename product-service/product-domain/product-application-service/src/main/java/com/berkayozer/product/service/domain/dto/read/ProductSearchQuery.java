package com.berkayozer.product.service.domain.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchQuery {
    private String name;
    private String brand;
    private String model;
    private String color;
    private UUID categoryId;
    private Boolean active; // We might only want to list the active ones

    // Pagination params
    private int page = 0;
    private int size = 10;
}