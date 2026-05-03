package com.berkayozer.cart.service.dataaccess.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// Redis'te "cart:{userId}" formatında kaydedilecek ve 7 gün (604800 saniye) sonra otomatik silinecek (TTL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "cart", timeToLive = 604800)
public class CartRedisEntity {
    @Id
    private UUID userId; // Redis Key olarak UserId kullanıyoruz
    private BigDecimal totalAmount;
    private List<CartItemRedisEntity> items;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemRedisEntity {
        private Long id;
        private UUID productId;
        private int quantity;
        private BigDecimal price;
        private BigDecimal subTotal;
    }
}