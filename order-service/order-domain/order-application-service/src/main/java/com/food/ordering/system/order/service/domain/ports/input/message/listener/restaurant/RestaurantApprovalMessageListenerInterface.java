package com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurant;

public interface RestaurantApprovalMessageListenerInterface {

    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
