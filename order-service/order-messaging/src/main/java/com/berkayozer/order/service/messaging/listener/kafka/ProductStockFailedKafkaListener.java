package com.berkayozer.order.service.messaging.listener.kafka;

import com.berkayozer.kafka.consumer.KafkaConsumer;
import com.berkayozer.kafka.order.avro.model.ProductStockFailedAvroModel;
import com.berkayozer.order.service.domain.exception.OrderNotFoundException;
import com.berkayozer.order.service.domain.ports.input.message.listener.ProductResponseMessageListener;
import com.berkayozer.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStockFailedKafkaListener implements KafkaConsumer<ProductStockFailedAvroModel> {

    private final ProductResponseMessageListener productResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "product-failed-container", topics = "product-stock-failed-topic")
    public void receive(@Payload List<ProductStockFailedAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} product failure responses received", messages.size());

        messages.forEach(avro -> {
            try {
                log.info("Processing unsuccessful stock reservation for order id: {} with failure messages: {}",
                        avro.getOrderId(), avro.getFailureMessages());

                productResponseMessageListener.productRejected(
                        orderMessagingDataMapper.productStockFailedAvroModelToProductResponse(avro));
            } catch (OptimisticLockingFailureException e) {
                log.warn("Caught optimistic locking exception in ProductStockFailedKafkaListener for order id: {}", avro.getOrderId());
            } catch (OrderNotFoundException e) {
                log.error("No order found for order id: {}", avro.getOrderId());
            }
        });
    }
}