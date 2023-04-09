package com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurant;

import com.food.ordering.system.application.ports.output.publisher.DomainEventPublisherInterface;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisherInterface extends DomainEventPublisherInterface<OrderPaidEvent> {
}
