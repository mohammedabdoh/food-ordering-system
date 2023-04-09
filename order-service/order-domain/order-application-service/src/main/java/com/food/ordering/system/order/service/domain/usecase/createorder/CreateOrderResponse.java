package com.food.ordering.system.order.service.domain.usecase.createorder;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class CreateOrderResponse {
    @NotNull
    private UUID orderId;

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    private String message;
}
