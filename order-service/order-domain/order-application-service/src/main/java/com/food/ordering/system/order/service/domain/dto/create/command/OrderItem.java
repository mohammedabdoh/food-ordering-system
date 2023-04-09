package com.food.ordering.system.order.service.domain.dto.create.command;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;
public record OrderItem(
        @NotNull UUID productId,
        @NotNull Integer quantity,
        @NotNull BigDecimal price,
        @NotNull BigDecimal subTotal
) {
}
