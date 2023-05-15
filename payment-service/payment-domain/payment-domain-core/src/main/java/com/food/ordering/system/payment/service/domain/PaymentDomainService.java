package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.Helper;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordering.system.payment.service.domain.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PaymentDomainService implements PaymentDomainServiceInterface {
    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry credit, List<CreditHistory> creditHistories, List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initializePayment();

        validateCreditEntry(payment, credit, failureMessages);
        subtractCreditEntity(payment, credit);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(credit, creditHistories, failureMessages);

        ZonedDateTime currentDateTime = Helper.DateTimeFactory.createUTCZonedDateTime();
        if(failureMessages.isEmpty()) {
            log.info("Payment initiated for order id {}", payment.getOrderId().toString());
            payment.updatePaymentStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, currentDateTime, failureMessages);
        } else {
            log.error("Payment initiation for order id {} has failed", payment.getOrderId().toString());
            payment.updatePaymentStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, currentDateTime, failureMessages);
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry credit, List<CreditHistory> creditHistories, List<String> failureMessages) {
        payment.validatePayment(failureMessages);

        addCreditEntry(payment, credit);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

        ZonedDateTime currentDateTime = Helper.DateTimeFactory.createUTCZonedDateTime();
        if(failureMessages.isEmpty()) {
            log.info("Payment is cancelled for order id {}", payment.getOrderId().toString());
            payment.updatePaymentStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, currentDateTime, failureMessages);
        } else {
            log.info("Payment cancellation for order id {}", payment.getOrderId().toString());
            payment.updatePaymentStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, currentDateTime, failureMessages);
        }
    }

    private void validateCreditEntry(Payment payment, CreditEntry credit, List<String> failureMessages) {
        if(payment.getPrice().isGreaterThan(credit.getTotalCredit())) {
            String failureMessage = String.format(
                    "Customer with id %s does not have enough credit for this payment", payment.getCustomerId().getValue()
            );
            log.error(failureMessage);
            failureMessages.add(failureMessage);
        }
    }

    private void subtractCreditEntity(Payment payment, CreditEntry credit) {
        credit.subtractCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories, TransactionType transactionType) {
        creditHistories.add(
                CreditHistory.builder()
                        .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                        .transactionType(transactionType)
                        .customerId(payment.getCustomerId())
                        .amount(payment.getPrice())
                        .build()
        );
    }

    private void validateCreditHistory(CreditEntry credit, List<CreditHistory> creditHistories, List<String> failureMessages) {
        Money totalCreditHistory = creditHistories
                .stream()
                .filter(creditHistory -> TransactionType.CREDIT == creditHistory.getTransactionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);

        Money totalDebitHistory = creditHistories
                .stream()
                .filter(creditHistory -> TransactionType.DEBIT == creditHistory.getTransactionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);

        if(totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            String failureMessage = String.format("Customer with id %s does not have enough credit according to the credit history", credit.getCustomerId().toString());
            log.error(failureMessage);
            failureMessages.add(failureMessage);
        }
    }

    private void addCreditEntry(Payment payment, CreditEntry credit) {
        credit.addCreditAmount(payment.getPrice());
    }
}
