package com.berkayozer.product.service.messaging.listener.kafka;

import com.berkayozer.kafka.order.avro.model.OrderCreatedAvroModel;
import com.berkayozer.product.service.domain.exception.ProductNotFoundException;
import com.berkayozer.product.service.domain.ports.input.message.listener.ProductMessageListener;
import com.berkayozer.product.service.messaging.mapper.ProductMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OrderCreatedKafkaListener {

    private final ProductMessageListener productMessageListener;
    private final ProductMessagingDataMapper productMessagingDataMapper;

    public OrderCreatedKafkaListener(ProductMessageListener productMessageListener,
                                     ProductMessagingDataMapper productMessagingDataMapper) {
        this.productMessageListener = productMessageListener;
        this.productMessagingDataMapper = productMessagingDataMapper;
    }

    @KafkaListener(id = "${kafka-consumer-config.order-created-consumer-group-id}",
            topics = "${product-service.order-created-topic-name}")
    public void receive(@Payload List<OrderCreatedAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of order created messages received", messages.size());

        messages.forEach(avroModel -> {
            try {
                log.info("Processing reserve stock for order id: {}", avroModel.getOrderId());
                productMessageListener.reserveProductStock(
                        productMessagingDataMapper.orderCreatedAvroModelToReserveStockCommand(avroModel));

            } catch (DataIntegrityViolationException e) {
                log.error("Caught unique constraint exception in OrderCreatedKafkaListener. Message is already processed.");
            } catch (OptimisticLockingFailureException e) {
                log.error("Caught optimistic locking exception in OrderCreatedKafkaListener.");
            } catch (ProductNotFoundException e) {
                log.error("No product found for the given order message!");
            } catch (Exception e) {
                log.error("Unexpected error while processing OrderCreatedKafkaListener", e);
            }
        });
    }
}