package com.berkayozer.order.service.domain.entity;

import com.berkayozer.domain.entity.AggregateRoot;
import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.OrderId;
import com.berkayozer.domain.valueobject.OrderStatus;
import com.berkayozer.domain.valueobject.UserId;
import com.berkayozer.order.service.domain.exception.OrderDomainException;
import com.berkayozer.order.service.domain.valueobject.OrderItemId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final UserId userId;
    private final Money price;
    private final List<OrderItem> items;

    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        userId = builder.userId;
        price = builder.price;
        items = builder.items;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    //  Siparişin İlk Kez Oluşturulması (PENDING)
    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem : items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    //  Fiyat ve Durum Doğrulaması (Cart'tan gelen verinin sağlaması)
    public void validateOrder() {
        if (orderStatus != null || getId() != null) {
            throw new OrderDomainException("Order is not in correct state for initialization!");
        }
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }

        Money itemsTotal = items.stream().map(OrderItem::getSubTotal).reduce(Money.ZERO, Money::add);
        if (!price.equals(itemsTotal)) {
            throw new OrderDomainException("Total price must equal sum of order items!");
        }
    }

    //  SAGA DURUM GEÇİŞLERİ (Orchestrator bu metodları çağıracak)

    // Product servisinden Stok Rezervasyon başarılı gelirse: PENDING -> STOCK_RESERVED
    public void reserveStock() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order must be in PENDING state to reserve stock!");
        }
        orderStatus = OrderStatus.STOCK_RESERVED;
    }

    // Payment servisinden Ödeme başarılı gelirse: STOCK_RESERVED -> PAID
    public void pay() {
        if (orderStatus != OrderStatus.STOCK_RESERVED) {
            throw new OrderDomainException("Order must be in STOCK_RESERVED state for payment!");
        }
        orderStatus = OrderStatus.PAID;
    }

    // Product servisinden Nihai Stok Onayı gelirse: PAID -> COMPLETED
    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order must be in PAID state for approval!");
        }
        orderStatus = OrderStatus.COMPLETED;
    }

    // Herhangi bir adımdan hata dönerse Siparişi Reddet: -> REJECTED
    public void reject(List<String> failureMessages) {
        if (orderStatus == OrderStatus.COMPLETED) {
            throw new OrderDomainException("Order cannot be rejected once it is completed!");
        }
        this.orderStatus = OrderStatus.REJECTED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> messages) {
        if (this.failureMessages != null && messages != null) {
            this.failureMessages.addAll(messages);
        }
        if (this.failureMessages == null) {
            this.failureMessages = messages;
        }
    }

    public UserId getUserId() { return userId; }
    public Money getPrice() { return price; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public List<String> getFailureMessages() { return failureMessages; }

    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private OrderId orderId;
        private UserId userId;
        private Money price;
        private List<OrderItem> items;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {}

        public Builder orderId(OrderId val) { orderId = val; return this; }
        public Builder userId(UserId val) { userId = val; return this; }
        public Builder price(Money val) { price = val; return this; }
        public Builder items(List<OrderItem> val) { items = val; return this; }
        public Builder orderStatus(OrderStatus val) { orderStatus = val; return this; }
        public Builder failureMessages(List<String> val) { failureMessages = val; return this; }

        public Order build() {
            return new Order(this);
        }
    }
}