package com.berkayozer.order.service.messaging.listener.kafka;

import com.berkayozer.kafka.consumer.KafkaConsumer;
import com.berkayozer.kafka.order.avro.model.ProductStockReservedAvroModel;
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
public class ProductStockReservedKafkaListener implements KafkaConsumer<ProductStockReservedAvroModel> {

    private final ProductResponseMessageListener productResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "product-reserved-container", topics = "product-stock-reserved-topic")
    public void receive(@Payload List<ProductStockReservedAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} product reservation responses received", messages.size());

        messages.forEach(avro -> {
            try {
                log.info("Processing successful stock reservation for order id: {}", avro.getOrderId());
                productResponseMessageListener.productReserved(
                        orderMessagingDataMapper.productStockReservedAvroModelToProductResponse(avro));
            } catch (OptimisticLockingFailureException e) {
                // TMO Felsefesi: Başka thread işlemi çoktan bitirdiyse, hatayı yut ki Kafka tekrar denemesin!
                log.warn("Caught optimistic locking exception in ProductStockReservedKafkaListener for order id: {}", avro.getOrderId());
            } catch (OrderNotFoundException e) {
                log.error("No order found for order id: {}", avro.getOrderId());
            }
        });
    }
}