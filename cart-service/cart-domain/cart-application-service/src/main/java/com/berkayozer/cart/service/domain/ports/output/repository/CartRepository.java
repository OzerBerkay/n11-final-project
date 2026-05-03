package com.berkayozer.cart.service.domain.ports.output.repository;

import com.berkayozer.cart.service.domain.entity.Cart;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Optional<Cart> findByUserId(UUID userId);
    Cart save(Cart cart);
    void deleteByUserId(UUID userId);
}