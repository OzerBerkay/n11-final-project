package com.berkayozer.order.service.domain.outbox.scheduler;

import com.berkayozer.order.service.domain.exception.OrderDomainException;
import com.berkayozer.order.service.domain.outbox.model.OrderOutboxMessage;
import com.berkayozer.order.service.domain.ports.output.repository.OrderOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxHelper {

    private final OrderOutboxRepository orderOutboxRepository;

    @Transactional(readOnly = true)
    public Optional<List<OrderOutboxMessage>> getOutboxMessagesByTypeAndStatus(String type, String status) {
        return orderOutboxRepository.findByTypeAndStatus(type, status);
    }

    @Transactional
    public void saveOutboxMessage(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxMessage response = orderOutboxRepository.save(orderOutboxMessage);
        if (response == null) {
            log.error("Could not save OrderOutboxMessage!");
            throw new OrderDomainException("Could not save OrderOutboxMessage!");
        }
        log.info("OrderOutboxMessage is saved with id: {}", orderOutboxMessage.getId());
    }

    @Transactional
    public void deleteOutboxMessageByStatus(String status) {
        orderOutboxRepository.deleteByStatus(status);
        log.info("Deleted OrderOutboxMessages with status: {}", status);
    }
}