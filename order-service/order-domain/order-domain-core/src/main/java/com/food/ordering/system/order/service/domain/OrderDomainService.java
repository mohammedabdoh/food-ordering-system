package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainService implements OrderDomainServiceInterface {
    public static final String UTC_TIME_ZONE = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);

        order.validateOrder();
        order.initializeOrder();

        log.info("An order with id {} has been initialized", order.getId().getValue());

        return new OrderCreatedEvent(order, getUtc());
    }

    @Override
    public OrderPaidEvent pay(Order order) {
        order.pay();

        log.info("Order with id {} has been paid", order.getId().getValue());

        return new OrderPaidEvent(order, getUtc());
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();

        log.info("Order with id {} has been approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);

        log.info("Order payment is cancelling for order id {}", order.getId().getValue());

        return new OrderCancelledEvent(order, getUtc());
    }
    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel();

        log.info("Order with id {} has been cancelled", order.getId().getValue());
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getOrderItems().forEach(orderItem -> {
            restaurant.getProducts().forEach(product -> {
                Product currentOrderItemProduct = orderItem.getProduct();
                if(currentOrderItemProduct.equals(product)) {
                    currentOrderItemProduct.updateProductWithNameAndPrice(product.getName(), product.getPrice());
                }
            });
        });
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive()) {
            throw new OrderDomainException(String.format("Restaurant with id %s is not active", restaurant.getId().getValue()));
        }
    }

    private static ZonedDateTime getUtc() {
        return ZonedDateTime.now(ZoneId.of(UTC_TIME_ZONE));
    }
}
