package com.berkayozer.cart.service.dataaccess.adapter;

import com.berkayozer.cart.service.dataaccess.entity.CartRedisEntity;
import com.berkayozer.cart.service.dataaccess.mapper.CartDataAccessMapper;
import com.berkayozer.cart.service.dataaccess.repository.CartRedisRepository;
import com.berkayozer.cart.service.domain.entity.Cart;
import com.berkayozer.cart.service.domain.ports.output.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final CartRedisRepository cartRedisRepository;
    private final CartDataAccessMapper cartDataAccessMapper;

    @Override
    public Optional<Cart> findByUserId(UUID userId) {
        return cartRedisRepository.findById(userId)
                .map(cartDataAccessMapper::cartRedisEntityToCart);
    }

    @Override
    public Cart save(Cart cart) {
        CartRedisEntity savedEntity = cartRedisRepository.save(cartDataAccessMapper.cartToCartRedisEntity(cart));
        return cartDataAccessMapper.cartRedisEntityToCart(savedEntity);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        cartRedisRepository.deleteById(userId);
    }
}