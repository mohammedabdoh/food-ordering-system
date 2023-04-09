package com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant;

import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;

import java.time.Instant;
import java.util.List;

public record RestaurantApprovalResponse(
        String id,
        String sagaId,
        String orderId,
        String restaurantId,
        Instant createdAt,
        OrderApprovalStatus orderApprovalStatus,
        List<String> failureMessages
) {
}
