package com.berkayozer.order.service.messaging.publisher.kafka;

import com.berkayozer.kafka.producer.KafkaMessageHelper;
import com.berkayozer.kafka.producer.service.KafkaProducer;
import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.event.OrderCancelledEvent;
import com.berkayozer.order.service.domain.outbox.model.OrderOutboxMessage;
import com.berkayozer.order.service.domain.ports.output.message.publisher.OrderOutboxMessagePublisher;
import com.berkayozer.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxKafkaPublisher implements OrderOutboxMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(OrderOutboxMessage outboxMessage, BiConsumer<OrderOutboxMessage, String> outboxCallback) {
        try {
            String type = outboxMessage.getType();
            String payload = outboxMessage.getPayload();

            if ("ORDER_CREATED".equals(type)) {
                Order order = objectMapper.readValue(payload, Order.class);
                var avroModel = orderMessagingDataMapper.orderToOrderCreatedAvroModel(order);

                log.info("Publishing FULL OrderCreated event to Kafka for order id: {}", order.getId().getValue());
                send("payment-request-topic", avroModel, outboxMessage, outboxCallback);
            }
            else if ("ORDER_CANCELLED".equals(type)) {
                OrderCancelledEvent event = objectMapper.readValue(payload, OrderCancelledEvent.class);
                var avroModel = orderMessagingDataMapper.orderCancelledEventToOrderCancelledAvroModel(event);

                log.info("Publishing FULL OrderCancelled event to Kafka for order id: {}", event.getOrder().getId().getValue());
                send("product-request-rollback-topic", avroModel, outboxMessage, outboxCallback);
            }
        } catch (Exception e) {
            log.error("Outbox publish error for aggregate id: {}", outboxMessage.getAggregateId(), e);
        }
    }

    private void send(String topic, SpecificRecordBase avro, OrderOutboxMessage message, BiConsumer<OrderOutboxMessage, String> callback) {
        kafkaProducer.send(topic, message.getAggregateId().toString(), avro,
                kafkaMessageHelper.getKafkaCallback(topic, avro, message,
                        (m, status) -> callback.accept(m, status.name()), // Senin KafkaHelper enum'unu, port'un String'ine bağlar[cite: 7]
                        message.getAggregateId().toString(), avro.getClass().getSimpleName()));
    }
}