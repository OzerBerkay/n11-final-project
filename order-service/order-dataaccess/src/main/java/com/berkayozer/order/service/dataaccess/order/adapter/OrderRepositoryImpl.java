package com.berkayozer.order.service.dataaccess.order.adapter;

import com.berkayozer.order.service.dataaccess.order.entity.OrderEntity;
import com.berkayozer.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.berkayozer.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.ports.output.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    @Override
    public Order save(Order order) {
        // Domain -> Entity çevrimi yapıp JPA üzerinden kaydediyoruz
        OrderEntity orderEntity = orderDataAccessMapper.orderToOrderEntity(order);
        OrderEntity savedOrderEntity = orderJpaRepository.save(orderEntity);
        // Kayıt sonrası tekrar Domain nesnesine çevirip dönüyoruz
        return orderDataAccessMapper.orderEntityToOrder(savedOrderEntity);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderJpaRepository.findById(orderId)
                .map(orderDataAccessMapper::orderEntityToOrder);
    }
}