package com.berkayozer.product.service.messaging.publisher.kafka;

import com.berkayozer.kafka.order.avro.model.ProductStockFailedAvroModel;
import com.berkayozer.kafka.order.avro.model.ProductStockReservedAvroModel;
import com.berkayozer.outbox.OutboxStatus;
import com.berkayozer.product.service.domain.outbox.model.ProductOutboxMessage;
import com.berkayozer.product.service.domain.ports.output.message.publisher.ProductStockMessagePublisher;
import com.berkayozer.product.service.messaging.mapper.ProductMessagingDataMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class ProductStockReservedKafkaPublisher implements ProductStockMessagePublisher {

    // KafkaAvroSerializer kullanacağımız için Object gönderiyoruz
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductMessagingDataMapper productMessagingDataMapper;
    private final ObjectMapper objectMapper;

    @Value("${product-service.product-stock-reserved-topic-name}")
    private String stockReservedTopic;

    @Value("${product-service.product-stock-failed-topic-name}")
    private String stockFailedTopic;

    public ProductStockReservedKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                              ProductMessagingDataMapper productMessagingDataMapper,
                                              ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.productMessagingDataMapper = productMessagingDataMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(ProductOutboxMessage outboxMessage,
                        BiConsumer<ProductOutboxMessage, OutboxStatus> outboxCallback) {
        String sagaId = outboxMessage.getSagaId().toString();
        log.info("Received ProductOutboxMessage for saga id: {} and type: {}", sagaId, outboxMessage.getType());

        try {
            // Outbox tablosundan gelen Payload String'ini JSON olarak okuyoruz
            JsonNode payload = objectMapper.readTree(outboxMessage.getPayload());

            // Type'a göre Avro Modeli yarat ve doğru kanala fırlat
            if (outboxMessage.getType().equals("STOCK_RESERVED")) {
                ProductStockReservedAvroModel avroModel = productMessagingDataMapper.getReservedAvroModel(sagaId, payload);
                kafkaTemplate.send(stockReservedTopic, sagaId, avroModel);
                log.info("ProductStockReservedAvroModel sent to kafka topic {} for saga id: {}", stockReservedTopic, sagaId);
            } else {
                ProductStockFailedAvroModel avroModel = productMessagingDataMapper.getFailedAvroModel(sagaId, payload);
                kafkaTemplate.send(stockFailedTopic, sagaId, avroModel);
                log.info("ProductStockFailedAvroModel sent to kafka topic {} for saga id: {}", stockFailedTopic, sagaId);
            }

            outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);

        } catch (Exception e) {
            log.error("Error while sending Product outbox message to kafka for saga id: {}, error: {}",
                    sagaId, e.getMessage());
            outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
        }
    }
}