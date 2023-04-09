package com.food.ordering.system.application.ports.output.publisher;

import com.food.ordering.system.domain.event.DomainEvent;

public interface DomainEventPublisherInterface<T extends DomainEvent> {
    void publish(T domainEvent);
}
