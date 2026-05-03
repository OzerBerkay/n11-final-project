package com.berkayozer.order.service.domain.mapper;

import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.domain.valueobject.UserId;
import com.berkayozer.order.service.domain.dto.create.CreateOrderResponse;
import com.berkayozer.order.service.domain.dto.query.OrderResponse;
import com.berkayozer.order.service.domain.dto.remote.CartItemResponse;
import com.berkayozer.order.service.domain.dto.remote.CartResponse;
import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    // Cart'tan gelen veriyi Order Entity'sine çevirir
    public Order cartResponseToOrder(CartResponse cartResponse) {
        return Order.builder()
                .userId(new UserId(cartResponse.getUserId()))
                .price(new Money(cartResponse.getTotalAmount()))
                .items(cartItemsToOrderItems(cartResponse.getItems()))
                .build();
    }

    private List<OrderItem> cartItemsToOrderItems(List<CartItemResponse> cartItems) {
        return cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .productId(new ProductId(cartItem.getProductId()))
                        .quantity(cartItem.getQuantity())
                        .price(new Money(cartItem.getPrice()))
                        .subTotal(new Money(cartItem.getSubTotal()))
                        .build())
                .collect(Collectors.toList());
    }

    // Order Entity'sini Controller'a döneceğimiz Response'a çevirir
    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getId().getValue()) // trackingId'yi sildiğimiz için direkt id dönüyoruz
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }

    public OrderResponse orderToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId().getValue())
                .userId(order.getUserId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }
}