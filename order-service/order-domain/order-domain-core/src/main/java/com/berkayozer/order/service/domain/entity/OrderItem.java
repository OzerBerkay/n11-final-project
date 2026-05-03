package com.berkayozer.order.service.domain.entity;

import com.berkayozer.domain.entity.BaseEntity;
import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.OrderId;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.order.service.domain.valueobject.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderId orderId; // Hangi siparişe ait olduğu
    private final ProductId productId; // Ürün referansı
    private final int quantity; // Adet
    private final Money price; // Birim fiyat
    private final Money subTotal; // Birim Fiyat * Adet

    // Sipariş ilklendirilirken çağrılacak
    void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }

    private OrderItem(Builder builder) {
        super.setId(builder.orderItemId);
        productId = builder.productId;
        quantity = builder.quantity;
        price = builder.price;
        subTotal = builder.subTotal;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public OrderId getOrderId() { return orderId; }
    public ProductId getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public Money getPrice() { return price; }
    public Money getSubTotal() { return subTotal; }

    public static final class Builder {
        private OrderItemId orderItemId;
        private ProductId productId;
        private int quantity;
        private Money price;
        private Money subTotal;

        private Builder() {}

        public Builder orderItemId(OrderItemId val) { orderItemId = val; return this; }
        public Builder productId(ProductId val) { productId = val; return this; }
        public Builder quantity(int val) { quantity = val; return this; }
        public Builder price(Money val) { price = val; return this; }
        public Builder subTotal(Money val) { subTotal = val; return this; }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}