package com.food.ordering.system.order.service.domain.ports.input.message.listener.payment;

import com.food.ordering.system.domain.valueobject.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PaymentResponse(
        String id,
        String sageId,
        String orderId,
        String paymentId,
        String customerId,
        BigDecimal price,
        Instant createdAt,
        PaymentStatus paymentStatus,
        List<String> failureMessages
) {
}
