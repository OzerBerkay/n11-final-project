package com.berkayozer.product.service.domain.outbox.scheduler;

import com.berkayozer.outbox.OutboxScheduler;
import com.berkayozer.outbox.OutboxStatus;
import com.berkayozer.product.service.domain.outbox.model.ProductOutboxMessage;
import com.berkayozer.product.service.domain.ports.output.message.publisher.ProductStockMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductOutboxScheduler implements OutboxScheduler {

    private final ProductOutboxHelper productOutboxHelper;
    private final ProductStockMessagePublisher productStockMessagePublisher;

    public ProductOutboxScheduler(ProductOutboxHelper productOutboxHelper,
                                  ProductStockMessagePublisher productStockMessagePublisher) {
        this.productOutboxHelper = productOutboxHelper;
        this.productStockMessagePublisher = productStockMessagePublisher;
    }

    // @Transactional anotasyonu burada değil updateOutboxMessage metodu icinde kullanılır.
    // Eğer burada kullanılırsa Kafka'ya 10 mesajın 9u başarılı 1'i başarısız giderse, hepsi başarısızmış gibi rollback olur
    // bu da duplicate event yayınlamayla sonuçlanır
    @Override
    //@Scheduled(fixedDelayString = "${product-service.outbox-scheduler-fixed-rate}", initialDelayString = "${product-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {
        List<ProductOutboxMessage> outboxMessages =
                productOutboxHelper.getProductOutboxMessageByOutboxStatus(OutboxStatus.STARTED);

        if (!outboxMessages.isEmpty()) {
            log.info("Received {} ProductOutboxMessage with id: {}, sending to message bus!",
                    outboxMessages.size(),
                    outboxMessages.stream().map(message -> message.getId().toString())
                            .collect(Collectors.joining(",")));

            // ARTIK DOĞRU PUBLISHER'I ÇAĞIRIYORUZ
            outboxMessages.forEach(outboxMessage ->
                    productStockMessagePublisher.publish(outboxMessage, productOutboxHelper::updateOutboxMessage));

            log.info("{} ProductOutboxMessage sent to message bus!", outboxMessages.size());
        }
    }
}