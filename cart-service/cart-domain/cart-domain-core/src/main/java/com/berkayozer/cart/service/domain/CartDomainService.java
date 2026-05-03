package com.berkayozer.cart.service.domain;

import com.berkayozer.cart.service.domain.entity.Cart;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.domain.valueobject.Money;

import java.util.Map;

public interface CartDomainService {
    void initializeNewCart(Cart cart);
    void updateCartPrices(Cart cart, Map<ProductId, Money> currentPrices);
}