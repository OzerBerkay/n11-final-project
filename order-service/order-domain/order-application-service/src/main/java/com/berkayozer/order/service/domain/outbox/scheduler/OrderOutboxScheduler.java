package com.berkayozer.order.service.domain.outbox.scheduler;

import com.berkayozer.order.service.domain.outbox.model.OrderOutboxMessage;
import com.berkayozer.order.service.domain.ports.output.message.publisher.OrderOutboxMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;
    private final OrderOutboxMessagePublisher outboxMessagePublisher;

    @Transactional
    @Scheduled(fixedDelayString = "10000", initialDelayString = "10000")
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>> outboxMessagesResponse =
                orderOutboxHelper.getOutboxMessagesByTypeAndStatus("ORDER_CREATED", "STARTED");

        if (outboxMessagesResponse.isPresent() && !outboxMessagesResponse.get().isEmpty()) {
            List<OrderOutboxMessage> outboxMessages = outboxMessagesResponse.get();
            log.info("Received {} OrderOutboxMessage with STARTED status", outboxMessages.size());

            outboxMessages.forEach(outboxMessage ->
                    outboxMessagePublisher.publish(outboxMessage, this::updateOutboxStatus)
            );
            log.info("{} OrderOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }

    private void updateOutboxStatus(OrderOutboxMessage orderOutboxMessage, String status) {
        orderOutboxMessage.setStatus(status);
        orderOutboxHelper.saveOutboxMessage(orderOutboxMessage);
        log.info("OrderOutboxMessage is updated with outbox status: {}", status);
    }
}