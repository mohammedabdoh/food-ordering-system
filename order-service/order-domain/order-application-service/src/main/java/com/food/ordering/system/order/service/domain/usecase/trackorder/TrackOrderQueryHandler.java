package com.food.ordering.system.order.service.domain.usecase.trackorder;

import com.food.ordering.system.application.ports.input.query.QueryHandlerInterface;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepositoryInterface;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class TrackOrderQueryHandler implements QueryHandlerInterface<TrackOrderResponse, TrackOrderQuery> {
    private final OrderDataMapper orderDataMapper;
    private final OrderRepositoryInterface orderRepository;

    public TrackOrderQueryHandler(OrderDataMapper orderDataMapper, OrderRepositoryInterface orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public TrackOrderResponse handle(TrackOrderQuery trackOrderQuery) {
        Optional<Order> order = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));

        if(order.isEmpty()) {
            String message = "Order %s not found";
            log.error(message);
            throw new OrderNotFoundException(message);
        }

        return orderDataMapper.createTrackOrderResponseFromOrderInstance(order.get());
    }
}
