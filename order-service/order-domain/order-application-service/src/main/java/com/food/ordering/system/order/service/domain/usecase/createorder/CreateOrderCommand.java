package com.food.ordering.system.order.service.domain.usecase.createorder;

import com.food.ordering.system.application.ports.input.command.Command;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class CreateOrderCommand extends Command {
    @NotNull
    private UUID customerId;
    @NotNull
    private UUID restaurantId;
    @NotNull
    private BigDecimal price;
    @NotNull
    private List<OrderItem> orderItems;
    @NotNull
    private OrderAddress orderAddress;
}
