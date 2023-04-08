package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends DomainEvent<Order> {
    public OrderCreatedEvent(Order entity, ZonedDateTime createdAt) {
        super(entity, createdAt);
    }
}
