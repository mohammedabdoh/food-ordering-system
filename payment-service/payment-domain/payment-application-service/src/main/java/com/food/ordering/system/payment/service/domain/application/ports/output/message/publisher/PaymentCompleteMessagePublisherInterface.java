package com.food.ordering.system.payment.service.domain.application.ports.output.message.publisher;

import com.food.ordering.system.application.ports.output.publisher.DomainEventPublisherInterface;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;

public interface PaymentCompleteMessagePublisherInterface extends DomainEventPublisherInterface<PaymentCompletedEvent> {
}
