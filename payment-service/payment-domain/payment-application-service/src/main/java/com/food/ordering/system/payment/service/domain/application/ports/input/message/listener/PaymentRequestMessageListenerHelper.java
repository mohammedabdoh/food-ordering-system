package com.food.ordering.system.payment.service.domain.application.ports.input.message.listener;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.payment.service.domain.PaymentDomainServiceInterface;
import com.food.ordering.system.payment.service.domain.application.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.application.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.application.ports.output.repository.CreditEntryRepositoryInterface;
import com.food.ordering.system.payment.service.domain.application.ports.output.repository.CreditHistoryRepositoryInterface;
import com.food.ordering.system.payment.service.domain.application.ports.output.repository.PaymentRepositoryInterface;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestMessageListenerHelper {
    private final PaymentDomainServiceInterface paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepositoryInterface paymentRepository;
    private final CreditEntryRepositoryInterface creditEntryRepository;
    private final CreditHistoryRepositoryInterface creditHistoryRepository;

    public PaymentRequestMessageListenerHelper(
            PaymentDomainServiceInterface paymentDomainService,
            PaymentDataMapper paymentDataMapper,
            PaymentRepositoryInterface paymentRepository,
            CreditEntryRepositoryInterface creditEntryRepository,
            CreditHistoryRepositoryInterface creditHistoryRepository
    ) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
    }

    @Transactional
    public PaymentEvent persistCompletedPayment(PaymentRequestMessage paymentRequestMessage) {
        log.info("Received payment complete event for payment {}", paymentRequestMessage.getId());

        Payment payment = paymentDataMapper.paymentFromPaymentRequestMessage(paymentRequestMessage);
        CreditEntry creditEntry = getCreditEntryForCustomer(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistoriesForCustomer(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistories, failureMessages);

        paymentRepository.save(payment);

        if(failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }

        return paymentEvent;
    }

    @Transactional
    public PaymentEvent persistCancelledPayment(PaymentRequestMessage paymentRequestMessage) {
        log.info("Received payment cancelled event for payment {}", paymentRequestMessage.getId());

        Payment payment = getPaymentForOrder(new OrderId(UUID.fromString(paymentRequestMessage.getOrderId())));
        CreditEntry creditEntry = getCreditEntryForCustomer(payment.getCustomerId());
        List<CreditHistory> creditHistories = getCreditHistoriesForCustomer(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages);

        paymentRepository.save(payment);

        if(failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }

        return paymentEvent;
    }

    private List<CreditHistory> getCreditHistoriesForCustomer(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories = creditHistoryRepository.findByCustomerId(customerId);

        if(creditHistories.isEmpty()) {
            String message = String.format("No credit histories found for customer %s", customerId.toString());
            log.error(message);
            throw new PaymentApplicationServiceException(message);
        }

        return creditHistories.get();
    }

    private CreditEntry getCreditEntryForCustomer(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if(creditEntry.isEmpty()) {
            String message = String.format("No credit entries found for customer %s", customerId.toString());
            log.error(message);
            throw new PaymentApplicationServiceException(message);
        }

        return creditEntry.get();
    }

    private Payment getPaymentForOrder(OrderId orderId) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);
        if(payment.isEmpty()) {
            String message = String.format("Could not find payment for order %s", orderId.toString());
            log.error(message);
            throw new PaymentApplicationServiceException(message);
        }
        return payment.get();
    }
}
