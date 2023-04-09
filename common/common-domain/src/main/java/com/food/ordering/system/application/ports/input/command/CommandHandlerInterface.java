package com.food.ordering.system.application.ports.input.command;

public interface CommandHandlerInterface<T, C> {
    T handle(C command);
}
