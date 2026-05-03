package com.berkayozer.order.service.domain.ports.input.service;

import com.berkayozer.order.service.domain.dto.create.CreateOrderCommand;
import com.berkayozer.order.service.domain.dto.create.CreateOrderResponse;
import com.berkayozer.order.service.domain.dto.query.OrderResponse;

import java.util.UUID;

public interface OrderApplicationService {
    // Dışarıdan sadece userId gelir, gerisini biz hallederiz!
    CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand);

    OrderResponse getOrderById(UUID orderId);
}