package com.berkayozer.product.service.domain.outbox.scheduler;

import com.berkayozer.outbox.OutboxStatus;
import com.berkayozer.product.service.domain.outbox.model.ProductOutboxMessage;
import com.berkayozer.product.service.domain.ports.output.repository.ProductOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.berkayozer.domain.DomainConstants.UTC;

@Slf4j
@Component
public class ProductOutboxHelper {

    private final ProductOutboxRepository productOutboxRepository;
    private final ObjectMapper objectMapper;

    public ProductOutboxHelper(ProductOutboxRepository productOutboxRepository, ObjectMapper objectMapper) {
        this.productOutboxRepository = productOutboxRepository;
        this.objectMapper = objectMapper;
    }

    // To save Saga's messages to the Outbox table
    @Transactional
    public void saveOrderOutboxMessage(Object payload, String type, UUID sagaId) {
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            ProductOutboxMessage outboxMessage = ProductOutboxMessage.builder()
                    .id(UUID.randomUUID())
                    .sagaId(sagaId)
                    .createdAt(ZonedDateTime.now(ZoneId.of(UTC)))
                    .type(type) // It will be either STOCK_RESERVED or STOCK_FAILED
                    .payload(payloadJson)
                    .outboxStatus(OutboxStatus.STARTED)
                    .version(0)
                    .build();

            productOutboxRepository.save(outboxMessage);
            log.info("ProductOutboxMessage saved with saga id: {} and type: {}", sagaId, type);
        } catch (JsonProcessingException e) {
            log.error("Could not create ProductOutboxMessage for saga id: {}", sagaId, e);
            throw new RuntimeException("Could not create ProductOutboxMessage", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductOutboxMessage> getProductOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        return productOutboxRepository.findByOutboxStatus(outboxStatus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateOutboxMessage(ProductOutboxMessage productOutboxMessage, OutboxStatus outboxStatus) {
        productOutboxMessage.setOutboxStatus(outboxStatus);
        productOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        productOutboxRepository.save(productOutboxMessage);
        log.info("ProductOutboxMessage is updated with outbox status: {}", outboxStatus.name());
    }

    @Transactional
    public void deleteProductOutboxMessageByOutboxStatus(OutboxStatus outboxStatus) {
        productOutboxRepository.deleteByOutboxStatus(outboxStatus);
    }

    @Transactional(readOnly = true)
    public boolean isOutboxMessageProcessed(UUID sagaId, String type) {
        return productOutboxRepository.findBySagaIdAndType(sagaId, type).isPresent();
    }
}