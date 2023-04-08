package com.food.ordering.system.domain.event;

import java.time.ZonedDateTime;

abstract public class DomainEvent<T> {
    private final T entity;
    private final ZonedDateTime createdAt;

    public DomainEvent(T entity, ZonedDateTime createdAt) {
        this.entity = entity;
        this.createdAt = createdAt;
    }

    public T getEntity() {
        return entity;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
