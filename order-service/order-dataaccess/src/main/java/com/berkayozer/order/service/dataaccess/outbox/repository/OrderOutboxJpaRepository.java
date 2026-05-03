package com.berkayozer.order.service.dataaccess.outbox.repository;

import com.berkayozer.order.service.dataaccess.outbox.entity.OrderOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutboxEntity, UUID> {

    // Poller için: Tipine ve durumuna göre getir
    Optional<List<OrderOutboxEntity>> findByTypeAndStatus(String type, String status);

    // Cleaner için: Durumuna göre sil
    void deleteByStatus(String status);
}