package com.berkayozer.order.service.application.rest;

import com.berkayozer.order.service.domain.dto.create.CreateOrderCommand;
import com.berkayozer.order.service.domain.dto.create.CreateOrderResponse;
import com.berkayozer.order.service.domain.dto.query.OrderResponse;
import com.berkayozer.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/json")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@AuthenticationPrincipal String userId) {
        // Jwt filtresi userId'yi direkt enjekte ediyor!
        log.info("Creating order for authenticated user: {}", userId);

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .userId(UUID.fromString(userId))
                .build();

        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);

        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID orderId,
                                                      @AuthenticationPrincipal String userId) {
        log.info("Retrieving order status for id: {}", orderId);

        OrderResponse orderResponse = orderApplicationService.getOrderById(orderId);

        // Güvenlik: Başkasının siparişini görmesini engelle
        if (!orderResponse.getUserId().toString().equals(userId)) {
            log.warn("User {} attempted to access order {} belonging to another user!", userId, orderId);
            return ResponseEntity.status(403).build(); // 403 Forbidden
        }

        return ResponseEntity.ok(orderResponse);
    }
}