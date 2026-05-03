package com.berkayozer.order.service.dataaccess.outbox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_outbox")
@Entity
public class OrderOutboxEntity {

    @Id
    private UUID id;
    private UUID aggregateId;
    private String type;
    private String payload;
    private String status;
    private ZonedDateTime createdAt;

    @Version // Optimistic Locking için (Race condition engellemek için)
    private int version;
}