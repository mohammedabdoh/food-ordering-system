package com.food.ordering.system.payment.service.domain.application.ports.input.message.listener;

public interface PaymentRequestMessageListenerInterface {
    void completePayment(PaymentRequestMessage paymentRequestMessage);
    void cancelPayment(PaymentRequestMessage paymentRequestMessage);
}
