package com.berkayozer.order.service.domain.dto.query;

import com.berkayozer.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponse {
    @NotNull
    private final UUID orderId;
    @NotNull
    private final UUID userId;
    @NotNull
    private final OrderStatus orderStatus;
    private final List<String> failureMessages; // Eğer sipariş REJECTED ise nedenini burada göreceğiz
}