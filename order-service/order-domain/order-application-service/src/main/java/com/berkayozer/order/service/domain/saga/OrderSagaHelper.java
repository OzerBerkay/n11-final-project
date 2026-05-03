package com.berkayozer.order.service.domain.saga;

import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.exception.OrderNotFoundException;
import com.berkayozer.order.service.domain.ports.output.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaHelper {

    private final OrderRepository orderRepository;

    public Order findOrder(String orderId) {
        Optional<Order> orderResponse = orderRepository.findById(UUID.fromString(orderId));
        if (orderResponse.isEmpty()) {
            log.error("Order with id: {} could not be found!", orderId);
            throw new OrderNotFoundException("Order with id " + orderId + " could not be found!");
        }
        return orderResponse.get();
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}