package com.food.ordering.system.order.service.domain.usecase.trackorder;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class TrackOrderResponse {
    @NotNull
    private UUID orderTrackingId;

    @NotNull
    private OrderStatus orderStatus;

    private List<String> failureMessages;
}
