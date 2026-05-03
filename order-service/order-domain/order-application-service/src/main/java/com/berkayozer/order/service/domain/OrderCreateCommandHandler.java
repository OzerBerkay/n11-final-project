package com.berkayozer.order.service.domain;

import com.berkayozer.order.service.domain.dto.create.CreateOrderCommand;
import com.berkayozer.order.service.domain.dto.create.CreateOrderResponse;
import com.berkayozer.order.service.domain.dto.remote.CartResponse;
import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.event.OrderCreatedEvent;
import com.berkayozer.order.service.domain.exception.OrderDomainException;
import com.berkayozer.order.service.domain.mapper.OrderDataMapper;
import com.berkayozer.order.service.domain.outbox.model.OrderOutboxMessage;
import com.berkayozer.order.service.domain.ports.output.remote.CartGateway;
import com.berkayozer.order.service.domain.ports.output.repository.OrderOutboxRepository;
import com.berkayozer.order.service.domain.ports.output.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CartGateway cartGateway;
    private final OrderDataMapper orderDataMapper;
    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper; // Eventi JSON'a çevirmek için

    // KRİTİK NOKTA: @Transactional sayesinde DB'ye yazarken hata olursa hepsi geri alınır!
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand command) {

        // 1. Cart Service'ten Kullanıcının Sepetini Çek
        CartResponse cartResponse = cartGateway.getCart(command.getUserId())
                .orElseThrow(() -> new OrderDomainException("Could not find cart for user: " + command.getUserId()));

        if (cartResponse.getItems() == null || cartResponse.getItems().isEmpty()) {
            throw new OrderDomainException("Cart is empty! Cannot create order.");
        }

        // 2. Cart DTO'sunu Order Entity'sine çevir
        Order order = orderDataMapper.cartResponseToOrder(cartResponse);

        // 3. Domain Kurallarını İşlet (PENDING duruma al ve ID'leri oluştur)
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order);

        // 4. Siparişi Veritabanına Kaydet
        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id: {}", savedOrder.getId().getValue());

        // 5. Outbox Message Oluştur ve Kaydet (Event Payload'u JSON'a çevriliyor)
        OrderOutboxMessage outboxMessage = getOrderOutboxMessage(orderCreatedEvent);
        orderOutboxRepository.save(outboxMessage);
        log.info("OrderCreatedEvent saved to outbox for order id: {}", savedOrder.getId().getValue());

        // 6. Yanıtı Dön
        return orderDataMapper.orderToCreateOrderResponse(savedOrder, "Order created successfully");
    }

    // Helper Metod: Event nesnesini Outbox kaydına dönüştürür
    private OrderOutboxMessage getOrderOutboxMessage(OrderCreatedEvent event) {
        String payload;
        try {
            // Gerçek projelerde bu payload'un içine sadece OrderId değil,
            // Product Service'in stok düşmesi için gereken ürün kalemleri (items) de konur.
            payload = objectMapper.writeValueAsString(event.getOrder());
        } catch (JsonProcessingException e) {
            log.error("Could not serialize OrderCreatedEvent payload!", e);
            throw new OrderDomainException("Could not serialize OrderCreatedEvent payload!", e);
        }

        return OrderOutboxMessage.builder()
                .id(UUID.randomUUID())
                .aggregateId(event.getOrder().getId().getValue())
                .type("ORDER_CREATED")
                .payload(payload)
                .status("STARTED") // Poller bunu görecek ve Kafka'ya basacak
                .createdAt(event.getCreatedAt())
                .build();
    }
}