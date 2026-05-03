package com.berkayozer.product.service.domain.outbox.scheduler;

import com.berkayozer.outbox.OutboxScheduler;
import com.berkayozer.outbox.OutboxStatus;
import com.berkayozer.product.service.domain.outbox.model.ProductOutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class ProductOutboxCleanerScheduler implements OutboxScheduler {

    private final ProductOutboxHelper productOutboxHelper;

    public ProductOutboxCleanerScheduler(ProductOutboxHelper productOutboxHelper) {
        this.productOutboxHelper = productOutboxHelper;
    }

    @Transactional
    @Scheduled(cron = "@midnight") // Triggers every midnight
    @Override
    public void processOutboxMessage() {
        List<ProductOutboxMessage> outboxMessages =
                productOutboxHelper.getProductOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

        if (!outboxMessages.isEmpty()) {
            log.info("Received {} ProductOutboxMessage for clean-up!", outboxMessages.size());

            productOutboxHelper.deleteProductOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);

            log.info("Deleted {} ProductOutboxMessage!", outboxMessages.size());
        }
    }
}