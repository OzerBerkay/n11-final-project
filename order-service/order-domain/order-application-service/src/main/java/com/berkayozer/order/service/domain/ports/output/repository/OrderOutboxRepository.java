package com.berkayozer.order.service.domain.ports.output.repository;

import com.berkayozer.order.service.domain.outbox.model.OrderOutboxMessage;

import java.util.List;
import java.util.Optional;

public interface OrderOutboxRepository {
    OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage);

    // Poller için gereken metod (Gönderilmeyenleri bul)
    Optional<List<OrderOutboxMessage>> findByTypeAndStatus(String type, String status);

    // Cleaner için gereken metod (İşi bitenleri sil)
    void deleteByStatus(String status);
}