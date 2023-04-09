package com.food.ordering.system.application.ports.input.query;

public interface QueryHandlerInterface<T, Q> {
    T handle(Q query);
}
