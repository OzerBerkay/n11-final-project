package com.berkayozer.cart.service.domain.valueobject;

import com.berkayozer.domain.valueobject.BaseId;

public class CartItemId extends BaseId<Long> {
    public CartItemId(Long value) {
        super(value);
    }
}