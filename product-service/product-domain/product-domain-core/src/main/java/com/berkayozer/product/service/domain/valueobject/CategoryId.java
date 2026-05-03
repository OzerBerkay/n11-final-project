package com.berkayozer.product.service.domain.valueobject;

import com.berkayozer.domain.valueobject.BaseId;

import java.util.UUID;

public class CategoryId extends BaseId<UUID> {
    public CategoryId(UUID value) {
        super(value);
    }
}