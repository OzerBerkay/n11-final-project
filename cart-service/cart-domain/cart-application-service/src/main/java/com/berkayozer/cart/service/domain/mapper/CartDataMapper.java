package com.berkayozer.cart.service.domain.mapper;

import com.berkayozer.cart.service.domain.dto.CartItemResponse;
import com.berkayozer.cart.service.domain.dto.CartResponse;
import com.berkayozer.cart.service.domain.entity.Cart;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartDataMapper {

    public CartResponse cartToCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> CartItemResponse.builder()
                        .productId(item.getProductId().getValue())
                        .quantity(item.getQuantity())
                        .price(item.getPrice().getAmount())
                        .subTotal(item.getSubTotal().getAmount())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .userId(cart.getUserId().getValue())
                .totalAmount(cart.getTotalAmount().getAmount())
                .items(itemResponses)
                .build();
    }
}