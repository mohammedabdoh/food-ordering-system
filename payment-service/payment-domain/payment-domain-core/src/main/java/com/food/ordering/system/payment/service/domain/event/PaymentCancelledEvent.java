package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentCancelledEvent extends PaymentEvent {
    public PaymentCancelledEvent(Payment entity, ZonedDateTime createdAt, List<String> failureMessages) {
        super(entity, createdAt, failureMessages);
    }
}
