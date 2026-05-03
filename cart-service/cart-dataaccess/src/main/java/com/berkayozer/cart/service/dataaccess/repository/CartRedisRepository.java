package com.berkayozer.cart.service.dataaccess.repository;

import com.berkayozer.cart.service.dataaccess.entity.CartRedisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRedisRepository extends CrudRepository<CartRedisEntity, UUID> {
    // CrudRepository bize save(), findById(), deleteById() gibi metotları otomatik sağlar.
}