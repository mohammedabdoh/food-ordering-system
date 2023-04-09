package com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.food.ordering.system.application.ports.output.publisher.DomainEventPublisherInterface;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisherInterface extends DomainEventPublisherInterface<OrderCreatedEvent> {
}
