package com.berkayozer.order.service.domain.ports.output.message.publisher;

import com.berkayozer.order.service.domain.outbox.model.OrderOutboxMessage;
import java.util.function.BiConsumer;

public interface OrderOutboxMessagePublisher {
    // Mesajı yayınlar ve Kafka'dan dönen (Başarılı/Başarısız) cevabına göre callback çalıştırır.
    void publish(OrderOutboxMessage orderOutboxMessage, BiConsumer<OrderOutboxMessage, String> outboxCallback);
}