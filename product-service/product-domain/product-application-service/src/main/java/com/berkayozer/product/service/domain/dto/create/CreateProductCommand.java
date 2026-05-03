package com.berkayozer.product.service.domain.dto.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateProductCommand {
    @NotBlank(message = "Name is required")
    private final String name;

    @NotBlank(message = "Description is required")
    private final String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be less than zero")
    private final BigDecimal price;

    @NotNull(message = "Initial stock is required")
    @Min(value = 0, message = "Initial stock cannot be less than zero")
    private final Integer initialStock;

    @NotNull(message = "Category ID is required")
    private final UUID categoryId;

    @NotBlank(message = "Brand is required")
    private final String brand;

    @NotBlank(message = "Model is required")
    private final String model;

    private final String color;

    @NotNull(message = "Image URL is required")
    private final String imageUrl;
}