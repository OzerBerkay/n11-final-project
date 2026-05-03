package com.berkayozer.cart.service.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class AddToCartCommand {
    private UUID userId;
    @NotNull
    private final UUID productId;
    @NotNull
    private final Integer quantity;
}