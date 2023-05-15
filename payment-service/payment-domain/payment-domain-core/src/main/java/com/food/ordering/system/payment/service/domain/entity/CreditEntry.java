package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntityId;

public class CreditEntry extends BaseEntity<CreditEntityId> {
    private final CustomerId customerId;
    private Money totalCredit;

    public void addCreditAmount(Money amount) {
        totalCredit = totalCredit.add(amount);
    }

    public void subtractCreditAmount(Money amount) {
        totalCredit = totalCredit.subtract(amount);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }
    public Money getTotalCredit() {
        return totalCredit;
    }

    private CreditEntry(Builder builder) {
        setId(builder.creditEntityId);
        customerId = builder.customerId;
        totalCredit = builder.totalCredit;
    }
    public static final class Builder {
        private CreditEntityId creditEntityId;
        private CustomerId customerId;
        private Money totalCredit;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder creditEntityId(CreditEntityId val) {
            creditEntityId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCredit(Money val) {
            totalCredit = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
