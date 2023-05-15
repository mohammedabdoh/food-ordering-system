package com.food.ordering.system.payment.service.domain.application.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.application.ports.input.message.listener.PaymentRequestMessage;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.valueobject.PaymentId;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class PaymentDataMapper {
    public Payment paymentFromPaymentRequestMessage(PaymentRequestMessage paymentRequestMessage) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequestMessage.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequestMessage.getCustomerId())))
                .price(new Money(paymentRequestMessage.getPrice()))
                .build();
    }
}
