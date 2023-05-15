package com.food.ordering.system.payment.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditEntityId extends BaseId<UUID> {
    public CreditEntityId(UUID value) {
        super(value);
    }
}
