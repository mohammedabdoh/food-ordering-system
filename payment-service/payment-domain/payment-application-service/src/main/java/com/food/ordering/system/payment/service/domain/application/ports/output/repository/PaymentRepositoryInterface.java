package com.food.ordering.system.payment.service.domain.application.ports.output.repository;

import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.payment.service.domain.entity.Payment;

import java.util.Optional;

public interface PaymentRepositoryInterface {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(OrderId orderId);

}
