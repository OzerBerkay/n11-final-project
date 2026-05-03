package com.berkayozer.order.service.domain;

import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.event.OrderCancelledEvent;
import com.berkayozer.order.service.domain.event.OrderCreatedEvent;
import com.berkayozer.order.service.domain.event.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order) {
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId().getValue());

        // Sipariş yaratıldı eventi fırlatılıyor (Outbox'a kaydedilecek)
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id: {} is paid", order.getId().getValue());

        // Ödeme başarılı eventi fırlatılıyor (Outbox'a kaydedilecek)
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id: {} is approved and completed", order.getId().getValue());
        // Son aşama olduğu için event dönmüyoruz (İhtiyaca göre eklenebilir)
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.reject(failureMessages);
        log.info("Order with id: {} is rejected/cancelled", order.getId().getValue());

        // Sipariş reddedildi/iptal edildi eventi fırlatılıyor (Outbox'a kaydedilecek)
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }
}