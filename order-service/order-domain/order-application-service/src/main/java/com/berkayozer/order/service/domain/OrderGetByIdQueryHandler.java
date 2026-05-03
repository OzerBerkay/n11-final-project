package com.berkayozer.order.service.domain;

import com.berkayozer.order.service.domain.dto.query.OrderResponse;
import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.exception.OrderNotFoundException;
import com.berkayozer.order.service.domain.mapper.OrderDataMapper;
import com.berkayozer.order.service.domain.ports.output.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderGetByIdQueryHandler {

    private final OrderRepository orderRepository;
    private final OrderDataMapper orderDataMapper;

    // Sadece okuma yapacağı için readOnly = true yapıyoruz, performansı artırır.
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(UUID orderId) {
        Optional<Order> orderResult = orderRepository.findById(orderId);

        if (orderResult.isEmpty()) {
            log.warn("Could not find order with id: {}", orderId);
            throw new OrderNotFoundException("Could not find order with id: " + orderId);
        }

        return orderDataMapper.orderToOrderResponse(orderResult.get());
    }
}