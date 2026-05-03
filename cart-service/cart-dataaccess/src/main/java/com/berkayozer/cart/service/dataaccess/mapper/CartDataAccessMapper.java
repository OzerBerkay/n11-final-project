package com.berkayozer.cart.service.dataaccess.mapper;

import com.berkayozer.cart.service.dataaccess.entity.CartRedisEntity;
import com.berkayozer.cart.service.domain.entity.Cart;
import com.berkayozer.cart.service.domain.entity.CartItem;
import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.domain.valueobject.UserId;
import com.berkayozer.cart.service.domain.valueobject.CartId;
import com.berkayozer.cart.service.domain.valueobject.CartItemId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartDataAccessMapper {

    // Domain objesini Redis objesine çevir
    public CartRedisEntity cartToCartRedisEntity(Cart cart) {
        List<CartRedisEntity.CartItemRedisEntity> itemEntities = new ArrayList<>();

        if (cart.getItems() != null) {
            itemEntities = cart.getItems().stream()
                    .map(item -> CartRedisEntity.CartItemRedisEntity.builder()
                            .id(item.getId().getValue())
                            .productId(item.getProductId().getValue())
                            .quantity(item.getQuantity())
                            .price(item.getPrice().getAmount())
                            .subTotal(item.getSubTotal().getAmount())
                            .build())
                    .collect(Collectors.toList());
        }

        return CartRedisEntity.builder()
                .userId(cart.getUserId().getValue())
                .totalAmount(cart.getTotalAmount().getAmount())
                .items(itemEntities)
                .build();
    }

    // Redis objesini Domain objesine çevir
    public Cart cartRedisEntityToCart(CartRedisEntity cartRedisEntity) {
        List<CartItem> items = new ArrayList<>();

        if (cartRedisEntity.getItems() != null) {
            items = cartRedisEntity.getItems().stream()
                    .map(itemEntity -> {
                        CartItem item = CartItem.builder()
                                .cartItemId(new CartItemId(itemEntity.getId()))
                                .productId(new ProductId(itemEntity.getProductId()))
                                .quantity(itemEntity.getQuantity())
                                .price(new Money(itemEntity.getPrice()))
                                .subTotal(new Money(itemEntity.getSubTotal()))
                                .build();
                        // cartId sonradan initialize edildiği için builder'da yollamıyoruz.
                        return item;
                    })
                    .collect(Collectors.toList());
        }

        return Cart.builder()
                .cartId(new CartId(cartRedisEntity.getUserId())) // Genelde sepet ID ile User ID aynı tutulur Redis'te
                .userId(new UserId(cartRedisEntity.getUserId()))
                .totalAmount(new Money(cartRedisEntity.getTotalAmount()))
                .items(items)
                .build();
    }
}