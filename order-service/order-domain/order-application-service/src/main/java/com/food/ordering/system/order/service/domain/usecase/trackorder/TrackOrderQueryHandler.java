package com.food.ordering.system.order.service.domain.usecase.trackorder;

import com.food.ordering.system.application.ports.input.query.QueryHandlerInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrackOrderQueryHandler implements QueryHandlerInterface<TrackOrderResponse, TrackOrderQuery> {
    @Override
    public TrackOrderResponse handle(TrackOrderQuery trackOrderQuery) {
        return null;
    }
}
