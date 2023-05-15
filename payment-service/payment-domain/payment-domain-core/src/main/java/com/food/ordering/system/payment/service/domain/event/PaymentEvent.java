package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentEvent extends DomainEvent<Payment> {
    private final List<String> failureMessages;

    public PaymentEvent(Payment entity, ZonedDateTime createdAt, List<String> failureMessages) {
        super(entity, createdAt);
        this.failureMessages = failureMessages;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
}
