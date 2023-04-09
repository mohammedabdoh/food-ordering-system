package com.food.ordering.system.order.service.domain.ports.input.message.listener.payment;

public interface PaymentResponseMessageListenerInterface {
    void paymentCompleted(PaymentResponse paymentResponse);
    void paymentCancelled(PaymentResponse paymentResponse);
}
