package com.food.ordering.system.order.service.domain.dto.create.response;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderResponse(
        @NotNull UUID orderId,
        @NotNull OrderStatus orderStatus,
        @NotNull String message
) {
}
