package com.food.ordering.system.order.service.domain.ports.input.message.listener.payment;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
public class PaymentResponseMessageListener implements PaymentResponseMessageListenerInterface {
    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {

    }
}
