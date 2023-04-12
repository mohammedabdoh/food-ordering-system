package com.food.ordering.system.order.service.application.rest;

import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationServiceInterface;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.usecase.trackorder.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.usecase.trackorder.TrackOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {
    private final OrderApplicationServiceInterface orderApplicationService;

    public OrderController(OrderApplicationServiceInterface orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
        log.info("Creating order for customer {} at restaurant {}", createOrderCommand.getCustomerId(), createOrderCommand.getRestaurantId());
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);

        log.info("Order created with tracking id {}", createOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> trackOrder(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder(
                TrackOrderQuery.builder().orderTrackingId(trackingId).build()
        );

        log.info("Found your order with status {}", trackOrderResponse.getOrderStatus());
        return ResponseEntity.ok(trackOrderResponse);
    }
}
