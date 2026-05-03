package com.berkayozer.order.service.domain.outbox.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxCleanerScheduler {

    private final OrderOutboxHelper orderOutboxHelper;

    // Her gece saat 02:00'de çalışır
    @Transactional
    @Scheduled(cron = "0 0 2 * * ?")
    public void processOutboxMessage() {
        log.info("OrderOutboxCleanerScheduler triggered. Cleaning up outbox table...");

        orderOutboxHelper.deleteOutboxMessageByStatus("COMPLETED");
        orderOutboxHelper.deleteOutboxMessageByStatus("FAILED");

        log.info("Outbox table cleanup finished!");
    }
}