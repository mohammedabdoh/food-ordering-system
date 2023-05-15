package com.food.ordering.system.payment.service.domain.application.ports.input.message.listener;

import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.application.ports.output.message.publisher.PaymentCancelledMessagePublisherInterface;
import com.food.ordering.system.payment.service.domain.application.ports.output.message.publisher.PaymentCompleteMessagePublisherInterface;
import com.food.ordering.system.payment.service.domain.application.ports.output.message.publisher.PaymentFailedMessagePublisherInterface;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentRequestMessageListener implements PaymentRequestMessageListenerInterface {
    private final PaymentRequestMessageListenerHelper paymentRequestMessageListenerHelper;
    private final PaymentCompleteMessagePublisherInterface paymentCompleteMessagePublisher;
    private final PaymentCancelledMessagePublisherInterface paymentCancelledMessagePublisher;
    private final PaymentFailedMessagePublisherInterface paymentFailedMessagePublisher;

    public PaymentRequestMessageListener(
            PaymentRequestMessageListenerHelper paymentRequestMessageListenerHelper,
            PaymentCompleteMessagePublisherInterface paymentCompleteMessagePublisher,
            PaymentCancelledMessagePublisherInterface paymentCancelledMessagePublisher,
            PaymentFailedMessagePublisherInterface paymentFailedMessagePublisher
    ) {
        this.paymentRequestMessageListenerHelper = paymentRequestMessageListenerHelper;
        this.paymentCompleteMessagePublisher = paymentCompleteMessagePublisher;
        this.paymentCancelledMessagePublisher = paymentCancelledMessagePublisher;
        this.paymentFailedMessagePublisher = paymentFailedMessagePublisher;
    }

    @Override
    public void completePayment(PaymentRequestMessage paymentRequestMessage) {
        PaymentEvent paymentEvent = paymentRequestMessageListenerHelper.persistCompletedPayment(paymentRequestMessage);
        paymentCompleteMessagePublisher.publish((PaymentCompletedEvent) paymentEvent);
    }

    @Override
    public void cancelPayment(PaymentRequestMessage paymentRequestMessage) {
        PaymentEvent paymentEvent = paymentRequestMessageListenerHelper.persistCompletedPayment(paymentRequestMessage);
        if(paymentEvent.getEntity().getPaymentStatus() == PaymentStatus.CANCELLED) {
            paymentCancelledMessagePublisher.publish((PaymentCancelledEvent) paymentEvent);
        } else if (paymentEvent.getEntity().getPaymentStatus() == PaymentStatus.FAILED) {
            paymentFailedMessagePublisher.publish((PaymentFailedEvent) paymentEvent);
        }
    }
}
