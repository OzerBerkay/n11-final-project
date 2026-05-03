package com.berkayozer.product.service.dataaccess.outbox.adapter;

import com.berkayozer.outbox.OutboxStatus;
import com.berkayozer.product.service.dataaccess.outbox.mapper.ProductOutboxDataAccessMapper;
import com.berkayozer.product.service.dataaccess.outbox.repository.ProductOutboxJpaRepository;
import com.berkayozer.product.service.domain.outbox.model.ProductOutboxMessage;
import com.berkayozer.product.service.domain.ports.output.repository.ProductOutboxRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductOutboxRepositoryImpl implements ProductOutboxRepository {

    private final ProductOutboxJpaRepository productOutboxJpaRepository;
    private final ProductOutboxDataAccessMapper productOutboxDataAccessMapper;

    public ProductOutboxRepositoryImpl(ProductOutboxJpaRepository productOutboxJpaRepository,
                                       ProductOutboxDataAccessMapper productOutboxDataAccessMapper) {
        this.productOutboxJpaRepository = productOutboxJpaRepository;
        this.productOutboxDataAccessMapper = productOutboxDataAccessMapper;
    }

    @Override
    public ProductOutboxMessage save(ProductOutboxMessage outboxMessage) {
        return productOutboxDataAccessMapper.outboxEntityToProductOutboxMessage(
                productOutboxJpaRepository.save(productOutboxDataAccessMapper
                        .productOutboxMessageToOutboxEntity(outboxMessage)));
    }

    @Override
    public List<ProductOutboxMessage> findByOutboxStatus(OutboxStatus outboxStatus) {
        return productOutboxJpaRepository.findByOutboxStatus(outboxStatus)
                .orElseGet(List::of)
                .stream()
                .map(productOutboxDataAccessMapper::outboxEntityToProductOutboxMessage)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByOutboxStatus(OutboxStatus outboxStatus) {
        productOutboxJpaRepository.deleteByOutboxStatus(outboxStatus);
    }

    @Override
    public Optional<ProductOutboxMessage> findBySagaIdAndType(UUID sagaId, String type) {
        return productOutboxJpaRepository.findBySagaIdAndType(sagaId, type)
                .map(productOutboxDataAccessMapper::outboxEntityToProductOutboxMessage);
    }
}