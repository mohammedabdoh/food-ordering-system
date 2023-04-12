package com.food.ordering.system.order.service.dataaccess.order.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(createAddressEntityFromOrderAddress(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .orderItems(convertOrderItemsToOrderItemsEntities(order.getOrderItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ? String.join(", ") : "")
                .build();

        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getOrderItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));

        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .deliveryAddress(createOrderDeliveryAddressFromAddressEntity(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .orderItems(convertOrderItemsEntitiesToOrderItems(orderEntity.getOrderItems()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(orderEntity.getFailureMessages().isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages().split(", "))))
                .build();
    }

    private List<OrderItemEntity> convertOrderItemsToOrderItemsEntities(List<OrderItem> orderItems) {
        return orderItems.stream().map(
                orderItem -> OrderItemEntity.builder()
                        .id(orderItem.getId().getValue())
                        .productId(orderItem.getProduct().getId().getValue())
                        .quantity(orderItem.getQuantity())
                        .price(orderItem.getPrice().getAmount())
                        .subTotal(orderItem.getSubTotal().getAmount())
                        .build()
        ).collect(Collectors.toList());
    }

    private List<OrderItem> convertOrderItemsEntitiesToOrderItems(List<OrderItemEntity> orderItemsEntities) {
        return orderItemsEntities.stream().map(
                orderItemEntity -> OrderItem.builder()
                        .orderItemId(new OrderItemId(orderItemEntity.getId()))
                        .product(new Product(new ProductId(orderItemEntity.getProductId())))
                        .price(new Money(orderItemEntity.getPrice()))
                        .quantity(orderItemEntity.getQuantity())
                        .subTotal(new Money(orderItemEntity.getSubTotal()))
                        .build()
        ).collect(Collectors.toList());
    }

    private OrderAddressEntity createAddressEntityFromOrderAddress(StreetAddress orderDeliveryAddress) {
        return OrderAddressEntity.builder()
                .id(orderDeliveryAddress.getId())
                .street(orderDeliveryAddress.getStreet())
                .postalCode(orderDeliveryAddress.getPostcode())
                .city(orderDeliveryAddress.getCity())
                .build();
    }

    private StreetAddress createOrderDeliveryAddressFromAddressEntity(OrderAddressEntity orderAddressEntity) {
        return new StreetAddress(
                UUID.fromString(orderAddressEntity.getId().toString()),
                orderAddressEntity.getStreet(),
                orderAddressEntity.getPostalCode(),
                orderAddressEntity.getCity()
        );
    }
}
