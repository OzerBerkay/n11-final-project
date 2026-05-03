package com.berkayozer.cart.service.domain.ports.input.service;

import com.berkayozer.cart.service.domain.dto.AddToCartCommand;
import com.berkayozer.cart.service.domain.dto.CartResponse;

import java.util.UUID;

public interface CartApplicationService {
    CartResponse getCart(UUID userId);
    CartResponse addToCart(AddToCartCommand command);
    CartResponse removeFromCart(UUID userId, UUID productId);
    void clearCart(UUID userId);
}