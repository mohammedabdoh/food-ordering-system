package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.usecase.trackorder.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {
    public Restaurant createRestaurantInstanceFromCreateOrderCommand(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(
                        createOrderCommand.getOrderItems().stream().map(orderItem -> new Product(new ProductId(orderItem.getProductId()))).collect(Collectors.toList())
                ).build();
    }

    public Order createOrderInstanceFromCreateOrderCommand(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(getStreetAddress(createOrderCommand))
                .price(new Money(createOrderCommand.getPrice()))
                .orderItems(getOrderItems(createOrderCommand))
                .build();
    }

    public CreateOrderResponse createOrderResponseFromOrderInstance(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getId().getValue())
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse createTrackOrderResponseFromOrderInstance(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    private static StreetAddress getStreetAddress(CreateOrderCommand createOrderCommand) {
        return new StreetAddress(
                UUID.randomUUID(),
                createOrderCommand.getOrderAddress().getStreet(),
                createOrderCommand.getOrderAddress().getPostalCode(),
                createOrderCommand.getOrderAddress().getCity()
        );
    }

    private static List<OrderItem> getOrderItems(CreateOrderCommand createOrderCommand) {
        return createOrderCommand.getOrderItems().stream().map(orderItem ->
                OrderItem.builder()
                        .product(new Product(new ProductId(orderItem.getProductId())))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQuantity())
                        .subTotal(new Money(orderItem.getSubTotal()))
                        .build()
        ).collect(Collectors.toList());
    }
}
