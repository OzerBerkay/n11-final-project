package com.berkayozer.order.service.domain;

import com.berkayozer.order.service.domain.dto.create.CreateOrderCommand;
import com.berkayozer.order.service.domain.dto.create.CreateOrderResponse;
import com.berkayozer.order.service.domain.dto.query.OrderResponse;
import com.berkayozer.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderGetByIdQueryHandler orderGetByIdQueryHandler;

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public OrderResponse getOrderById(UUID orderId) {
        return orderGetByIdQueryHandler.getOrderById(orderId);
    }
}