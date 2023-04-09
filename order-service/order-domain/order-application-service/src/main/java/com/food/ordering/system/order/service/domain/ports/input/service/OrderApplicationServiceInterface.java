package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.usecase.trackorder.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.usecase.trackorder.TrackOrderResponse;
import jakarta.validation.Valid;

public interface OrderApplicationServiceInterface {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
