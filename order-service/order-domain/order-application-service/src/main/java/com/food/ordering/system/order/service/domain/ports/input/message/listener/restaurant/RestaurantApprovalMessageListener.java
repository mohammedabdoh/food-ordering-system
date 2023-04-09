package com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Component
public class RestaurantApprovalMessageListener implements RestaurantApprovalMessageListenerInterface {
    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
