package com.food.ordering.system.application.ports.input.command;

import java.time.Instant;

abstract public class Command {
    protected Instant receivedAt;
}
