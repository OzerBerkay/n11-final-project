package com.berkayozer.order.service.dataaccess.order.mapper;

import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.OrderId;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.domain.valueobject.UserId;
import com.berkayozer.order.service.dataaccess.order.entity.OrderEntity;
import com.berkayozer.order.service.dataaccess.order.entity.OrderItemEntity;
import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.entity.OrderItem;
import com.berkayozer.order.service.domain.valueobject.OrderItemId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .userId(order.getUserId().getValue())
                .price(order.getPrice().getAmount())
                .orderStatus(order.getOrderStatus())
                // Liste halindeki hata mesajlarını aralarına virgül koyarak tek bir String yapar
                .failureMessages(order.getFailureMessages() != null ?
                        String.join(",", order.getFailureMessages()) : "")
                .items(orderItemsToOrderItemEntities(order.getItems()))
                .build();

        // JPA'da ManyToOne ilişkisinin kurulması için her item'a ana order'ı set ediyoruz
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .userId(new UserId(orderEntity.getUserId()))
                .price(new Money(orderEntity.getPrice()))
                .orderStatus(orderEntity.getOrderStatus())
                // Veritabanındaki String'i listeye çeviren güvenli metodumuzu çağırıyoruz
                .failureMessages(mapFailureMessages(orderEntity.getFailureMessages()))
                .items(orderItemEntitiesToOrderItems(orderEntity.getItems()))
                .build();
    }

    private List<String> mapFailureMessages(String failureMessages) {
        if (failureMessages == null || failureMessages.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(failureMessages.split(",")));
    }

    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> items) {
        return items.stream()
                .map(item -> OrderItemEntity.builder()
                        .id(item.getId().getValue())
                        .productId(item.getProductId().getValue())
                        .price(item.getPrice().getAmount())
                        .quantity(item.getQuantity())
                        .subTotal(item.getSubTotal().getAmount())
                        .build())
                .collect(Collectors.toList());
    }

    private List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> items) {
        return items.stream()
                .map(item -> OrderItem.builder()
                        .orderItemId(new OrderItemId(item.getId()))
                        .productId(new ProductId(item.getProductId()))
                        .price(new Money(item.getPrice()))
                        .quantity(item.getQuantity())
                        .subTotal(new Money(item.getSubTotal()))
                        .build())
                .collect(Collectors.toList());
    }
}