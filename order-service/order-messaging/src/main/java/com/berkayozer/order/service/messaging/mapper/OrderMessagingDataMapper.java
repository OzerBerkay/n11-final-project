package com.berkayozer.order.service.messaging.mapper;

import com.berkayozer.kafka.order.avro.model.*;
import com.berkayozer.order.service.domain.dto.message.ProductResponse;
import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.event.OrderCancelledEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

    // Artık Siparişin TAMAMI fırlatılıyor!
    public OrderCreatedAvroModel orderToOrderCreatedAvroModel(Order order) {
        return OrderCreatedAvroModel.newBuilder()
                .setSagaId(UUID.randomUUID())
                .setOrderId(order.getId().getValue())
                //dahası eklenecek
                .build();
    }

    // Kompansasyon (Rollback) durumunda da Siparişin TAMAMI iptal için gönderiliyor!
    public OrderCancelledAvroModel orderCancelledEventToOrderCancelledAvroModel(OrderCancelledEvent event) {
        Order order = event.getOrder();
        return OrderCancelledAvroModel.newBuilder()
                .setSagaId(UUID.randomUUID())
                .setOrderId(order.getId().getValue())
                // Dahası eklenecek
                .build();
    }

    // Gelen Product Event'lerini Uygulamanın DTO'larına Çevir
    public ProductResponse productStockReservedAvroModelToProductResponse(ProductStockReservedAvroModel avro) {
        return ProductResponse.builder()
                .orderId(avro.getOrderId().toString())
                .sagaId(avro.getSagaId().toString())
                .status("RESERVED")
                .build();
    }

    public ProductResponse productStockFailedAvroModelToProductResponse(ProductStockFailedAvroModel avro) {
        return ProductResponse.builder()
                .orderId(avro.getOrderId().toString())
                .sagaId(avro.getSagaId().toString())
                .status("FAILED")
                .failureMessages(java.util.Collections.singletonList(avro.getFailureMessages()))
                .build();
    }
}