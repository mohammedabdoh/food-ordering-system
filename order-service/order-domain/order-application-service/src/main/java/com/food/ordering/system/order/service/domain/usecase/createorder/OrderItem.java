package com.food.ordering.system.order.service.domain.usecase.createorder;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class OrderItem {
    @NotNull
    private UUID productId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;
    @NotNull
    private BigDecimal subTotal;
}
