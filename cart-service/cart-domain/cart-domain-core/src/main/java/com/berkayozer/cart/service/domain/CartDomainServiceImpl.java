package com.berkayozer.cart.service.domain;

import com.berkayozer.cart.service.domain.entity.Cart;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.domain.valueobject.Money;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class CartDomainServiceImpl implements CartDomainService {

    @Override
    public void initializeNewCart(Cart cart) {
        cart.initializeCart();
        log.info("New cart initialized for user: {}", cart.getUserId().getValue());
    }

    @Override
    public void updateCartPrices(Cart cart, Map<ProductId, Money> currentPrices) {
        cart.updatePricesAndCalculateTotal(currentPrices);
        log.info("Cart prices updated for user: {}", cart.getUserId().getValue());
    }
}