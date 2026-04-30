package com.berkayozer.user.service.domain.valueobject;

import com.berkayozer.domain.valueobject.BaseId;
import java.util.UUID;

public class AddressId extends BaseId<UUID> {
    public AddressId(UUID value) {
        super(value);
    }
}