package com.berkayozer.product.service.messaging.mapper;

import com.berkayozer.kafka.order.avro.model.OrderCancelledAvroModel;
import com.berkayozer.kafka.order.avro.model.OrderCreatedAvroModel;
import com.berkayozer.kafka.order.avro.model.ProductStockFailedAvroModel;
import com.berkayozer.kafka.order.avro.model.ProductStockReservedAvroModel;
import com.berkayozer.product.service.domain.dto.message.ReleaseStockCommand;
import com.berkayozer.product.service.domain.dto.message.ReserveStockCommand;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductMessagingDataMapper {

    // AVRO'dan DOMAIN COMMAND'e Dönüşümler (GELEN MESAJLAR İÇİN)
    public ReserveStockCommand orderCreatedAvroModelToReserveStockCommand(OrderCreatedAvroModel avroModel) {
        return ReserveStockCommand.builder()
                // Domain'de sagaId String olduğu için .toString() yapıyoruz
                .sagaId(avroModel.getSagaId().toString())
                // Avro zaten UUID döndüğü için doğrudan kullanabiliriz
                .orderId(avroModel.getOrderId())
                .productId(avroModel.getProductId())
                .quantity(avroModel.getQuantity())
                .build();
    }

    public ReleaseStockCommand orderCancelledAvroModelToReleaseStockCommand(OrderCancelledAvroModel avroModel) {
        return ReleaseStockCommand.builder()
                // Domain'de sagaId String olduğu için .toString() yapıyoruz
                .sagaId(avroModel.getSagaId().toString())
                // Avro zaten UUID döndüğü için doğrudan kullanabiliriz
                .orderId(avroModel.getOrderId())
                .productId(avroModel.getProductId())
                .quantity(avroModel.getQuantity())
                .build();
    }

    // OUTBOX PAYLOAD'undan AVRO'ya Dönüşümler (GİDEN MESAJLAR İÇİN)
    public ProductStockReservedAvroModel getReservedAvroModel(String sagaId, JsonNode payload) {
        return ProductStockReservedAvroModel.newBuilder()
                // Avro nesnesi UUID beklediği için String'leri dönüştürüyoruz
                .setSagaId(UUID.fromString(sagaId))
                .setOrderId(UUID.fromString(payload.get("orderId").asText()))
                .setProductId(UUID.fromString(payload.get("productId").asText()))
                .build();
    }

    public ProductStockFailedAvroModel getFailedAvroModel(String sagaId, JsonNode payload) {
        return ProductStockFailedAvroModel.newBuilder()
                // Avro nesnesi UUID beklediği için String'leri dönüştürüyoruz
                .setSagaId(UUID.fromString(sagaId))
                .setOrderId(UUID.fromString(payload.get("orderId").asText()))
                .setProductId(UUID.fromString(payload.get("productId").asText()))
                .setFailureMessages(payload.get("failureMessages").asText())
                .build();
    }
}