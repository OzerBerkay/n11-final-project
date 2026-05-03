package com.berkayozer.order.service.domain.event;

import com.berkayozer.order.service.domain.entity.Order;
import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {
    public OrderCancelledEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}