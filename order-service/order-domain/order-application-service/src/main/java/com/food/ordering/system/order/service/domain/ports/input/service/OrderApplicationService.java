package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.application.ports.input.command.CommandHandlerInterface;
import com.food.ordering.system.application.ports.input.query.QueryHandlerInterface;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderCommandHandler;
import com.food.ordering.system.order.service.domain.usecase.createorder.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.usecase.trackorder.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.usecase.trackorder.TrackOrderQueryHandler;
import com.food.ordering.system.order.service.domain.usecase.trackorder.TrackOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
class OrderApplicationService implements OrderApplicationServiceInterface {
    private final CommandHandlerInterface<CreateOrderResponse, CreateOrderCommand> createOrderCommandHandler;
    private final QueryHandlerInterface<TrackOrderResponse, TrackOrderQuery> trackOrderQueryHandler;

    public OrderApplicationService(
            CreateOrderCommandHandler createOrderCommandHandler,
            TrackOrderQueryHandler trackOrderQueryHandler
    ) {
        this.createOrderCommandHandler = createOrderCommandHandler;
        this.trackOrderQueryHandler = trackOrderQueryHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return createOrderCommandHandler.handle(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return trackOrderQueryHandler.handle(trackOrderQuery);
    }
}
