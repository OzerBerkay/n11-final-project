package com.berkayozer.order.service.domain.saga;

import com.berkayozer.domain.valueobject.OrderStatus;
import com.berkayozer.order.service.domain.OrderDomainService;
import com.berkayozer.order.service.domain.dto.message.PaymentResponse;
import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.event.OrderCancelledEvent;
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
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

    private final OrderSagaHelper orderSagaHelper;
    private final OrderOutboxHelper orderOutboxHelper;
    private final OrderDomainService orderDomainService; // Domain olaylarını tetiklemek için eklendi
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void process(PaymentResponse paymentResponse) {
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());

        // 1. İdempotency: Daha önce ödeme işlenmiş mi?
        if (order.getOrderStatus() == OrderStatus.PAID || order.getOrderStatus() == OrderStatus.COMPLETED) {
            log.info("Order with id: {} is already paid! Ignoring the PaymentResponse.", order.getId().getValue());
            return;
        }

        // Beklenmeyen durum kontrolü
        if (order.getOrderStatus() != OrderStatus.STOCK_RESERVED) {
            log.warn("Order with id: {} is in invalid state: {} for PaymentResponse processing!",
                    order.getId().getValue(), order.getOrderStatus());
            return;
        }

        log.info("Processing successful payment for order id: {}", paymentResponse.getOrderId());

        // 2. Siparişi PAID durumuna çek
        order.pay();
        orderSagaHelper.saveOrder(order);

        // 3. İsteğe bağlı: Onay (Approval) adımı varsa yeni outbox mesajı fırlatılır.
        // Eğer sistemde PAID son adımsa, order.approve() çağrılıp COMPLETED yapılabilir.
        // Biz genişletilebilir olması adına SAGA'yı devam ettiriyoruz.
        OrderOutboxMessage outboxMessage = getOutboxMessage(order, "ORDER_PAID");
        orderOutboxHelper.saveOutboxMessage(outboxMessage);

        log.info("Order with id: {} is transitioning to PAID state", order.getId().getValue());
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());

        // 1. İdempotency: Zaten reddedilmiş mi?
        if (order.getOrderStatus() == OrderStatus.REJECTED || order.getOrderStatus() == OrderStatus.CANCELLED) {
            log.info("Order with id: {} is already cancelled/rejected! Ignoring the PaymentResponse rollback.", order.getId().getValue());
            return;
        }

        log.info("Cancelling order with id: {} due to payment failure", paymentResponse.getOrderId());

        // 2. Domain Service'i kullanarak siparişi iptal et ve Event nesnesini al!
        OrderCancelledEvent domainEvent = orderDomainService.cancelOrderPayment(order, paymentResponse.getFailureMessages());

        // 3. Siparişi Reddet (Güncel haliyle DB'ye yaz)
        orderSagaHelper.saveOrder(order);

        // 4. KOMPANSASYON (Geri Alma): Outbox'a Order (Entity) değil, OrderCancelledEvent (Domain Event) yaz!
        OrderOutboxMessage outboxMessage = getOutboxMessageFromEvent(domainEvent, "ORDER_CANCELLED");
        orderOutboxHelper.saveOutboxMessage(outboxMessage);

        log.info("Order with id: {} is rejected. Compensation message sent to outbox.", order.getId().getValue());
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
            log.error("Could not serialize payload for saga payment step!", e);
            throw new OrderDomainException("Could not serialize payload!", e);
        }
    }

    // YENİ METOD: Order nesnesi yerine Event nesnesini serileştirir
    private OrderOutboxMessage getOutboxMessageFromEvent(OrderCancelledEvent event, String type) {
        try {
            return OrderOutboxMessage.builder()
                    .id(UUID.randomUUID())
                    .aggregateId(event.getOrder().getId().getValue())
                    .type(type)
                    // DİKKAT: Artık Event'in kendisi payload oluyor.
                    // Product Service bu event'i alıp içinden ürünleri okuyup stoğu artıracak.
                    .payload(objectMapper.writeValueAsString(event))
                    .status(OutboxStatus.STARTED.name())
                    .createdAt(event.getCreatedAt())
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Could not serialize payload for saga payment rollback step!", e);
            throw new OrderDomainException("Could not serialize payload!", e);
        }
    }
}