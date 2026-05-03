package com.berkayozer.order.service.domain.saga;

import com.berkayozer.domain.valueobject.OrderStatus;
import com.berkayozer.order.service.domain.dto.message.ProductResponse;
import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.exception.OrderDomainException;
import com.berkayozer.order.service.domain.outbox.model.OrderOutboxMessage;
import com.berkayozer.order.service.domain.outbox.scheduler.OrderOutboxHelper;
import com.berkayozer.outbox.OutboxStatus;
import com.berkayozer.saga.SagaStep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProductSaga implements SagaStep<ProductResponse> {

    private final OrderSagaHelper orderSagaHelper;
    private final OrderOutboxHelper orderOutboxHelper;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void process(ProductResponse productResponse) {
        Order order = orderSagaHelper.findOrder(productResponse.getOrderId());

        // 1. İdempotency Kontrolü: Bu mesaj daha önce işlenmiş mi?
        if (order.getOrderStatus() == OrderStatus.STOCK_RESERVED ||
                order.getOrderStatus() == OrderStatus.PAID ||
                order.getOrderStatus() == OrderStatus.COMPLETED) {
            log.info("Order with id: {} is already processed! Ignoring the ProductResponse.", order.getId().getValue());
            return; // Hiçbir şey yapmadan çık. Sistem güvende.
        }

        // Beklenmeyen bir durumdaysa (örn: REJECTED olmuşsa ama process gelmişse)
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            log.warn("Order with id: {} is in invalid state: {} for ProductResponse processing!",
                    order.getId().getValue(), order.getOrderStatus());
            return;
        }

        log.info("Processing successful product reservation for order id: {}", productResponse.getOrderId());

        order.reserveStock();
        orderSagaHelper.saveOrder(order);

        // 2. Bir önceki Outbox mesajını (ORDER_CREATED) tamamlandı olarak işaretle
        completePreviousOutboxMessage(order.getId().getValue().toString(), "ORDER_CREATED");

        // 3. Yeni Outbox mesajını (PAYMENT_STARTED) oluştur
        OrderOutboxMessage outboxMessage = getOutboxMessage(order, "PAYMENT_STARTED");
        orderOutboxHelper.saveOutboxMessage(outboxMessage);

        log.info("Order with id: {} is transitioning to STOCK_RESERVED state", order.getId().getValue());
    }

    @Override
    @Transactional
    public void rollback(ProductResponse productResponse) {
        Order order = orderSagaHelper.findOrder(productResponse.getOrderId());

        // 1. İdempotency Kontrolü: Zaten reddedilmiş mi?
        if (order.getOrderStatus() == OrderStatus.REJECTED || order.getOrderStatus() == OrderStatus.CANCELLED) {
            log.info("Order with id: {} is already cancelled/rejected! Ignoring the ProductResponse.", order.getId().getValue());
            return;
        }

        log.info("Cancelling order with id: {} due to product failure", productResponse.getOrderId());

        order.reject(productResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);

        // Bir önceki Outbox mesajını FAILED olarak işaretle (İsteğe bağlı, süreci bitirmek için)
        failPreviousOutboxMessage(order.getId().getValue().toString(), "ORDER_CREATED");

        log.info("Order with id: {} is cancelled/rejected", order.getId().getValue());
    }

    private void completePreviousOutboxMessage(String orderId, String type) {
        // Bu helper metodunu OrderOutboxHelper içine eklemen gerekecek
        Optional<List<OrderOutboxMessage>> messages = orderOutboxHelper.getOutboxMessagesByTypeAndStatus(type, OutboxStatus.COMPLETED.name());
        // ... (bulunan mesajların status'unu COMPLETED yapıp kaydetme mantığı)
        // Şimdilik mantıksal akışı göstermek için placeholder olarak bıraktım.
    }

    private void failPreviousOutboxMessage(String orderId, String type) {
        // ... (bulunan mesajların status'unu FAILED yapıp kaydetme mantığı)
    }

    private OrderOutboxMessage getOutboxMessage(Order order, String type) {
        try {
            return OrderOutboxMessage.builder()
                    .id(UUID.randomUUID())
                    .aggregateId(order.getId().getValue())
                    .type(type)
                    .payload(objectMapper.writeValueAsString(order))
                    .status(OutboxStatus.STARTED.name())
                    .createdAt(ZonedDateTime.now(ZoneId.of("UTC")))
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Could not serialize payload for saga step!", e);
            throw new OrderDomainException("Could not serialize payload!", e);
        }
    }
}