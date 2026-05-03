package com.berkayozer.order.service.dataaccess.outbox.adapter;

import com.berkayozer.order.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.berkayozer.order.service.dataaccess.outbox.mapper.OrderOutboxDataAccessMapper;
import com.berkayozer.order.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import com.berkayozer.order.service.domain.outbox.model.OrderOutboxMessage;
import com.berkayozer.order.service.domain.ports.output.repository.OrderOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {

    private final OrderOutboxJpaRepository orderOutboxJpaRepository;
    private final OrderOutboxDataAccessMapper orderOutboxDataAccessMapper;

    @Override
    public OrderOutboxMessage save(OrderOutboxMessage orderOutboxMessage) {
        OrderOutboxEntity entity = orderOutboxDataAccessMapper.orderOutboxMessageToOrderOutboxEntity(orderOutboxMessage);
        return orderOutboxDataAccessMapper.orderOutboxEntityToOrderOutboxMessage(
                orderOutboxJpaRepository.save(entity)
        );
    }

    @Override
    public Optional<List<OrderOutboxMessage>> findByTypeAndStatus(String type, String status) {
        return orderOutboxJpaRepository.findByTypeAndStatus(type, status)
                .map(entities -> entities.stream()
                        .map(orderOutboxDataAccessMapper::orderOutboxEntityToOrderOutboxMessage)
                        .collect(Collectors.toList()));
    }

    @Override
    public void deleteByStatus(String status) {
        orderOutboxJpaRepository.deleteByStatus(status);
    }
}