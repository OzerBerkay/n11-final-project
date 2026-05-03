package com.berkayozer.cart.service.domain.valueobject;

import com.berkayozer.domain.valueobject.BaseId;
import java.util.UUID;

public class CartId extends BaseId<UUID> {
    public CartId(UUID value) {
        super(value);
    }
}