package com.berkayozer.order.service.domain.ports.output.remote;

import com.berkayozer.order.service.domain.dto.remote.CartResponse;

import java.util.Optional;
import java.util.UUID;

public interface CartGateway {
    // Kullanıcının sepetini getirir
    Optional<CartResponse> getCart(UUID userId);
}
