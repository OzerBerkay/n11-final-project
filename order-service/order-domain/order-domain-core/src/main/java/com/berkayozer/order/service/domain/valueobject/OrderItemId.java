package com.berkayozer.order.service.domain.valueobject;

import com.berkayozer.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}