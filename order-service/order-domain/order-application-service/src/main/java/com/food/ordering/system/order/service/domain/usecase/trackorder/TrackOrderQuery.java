package com.food.ordering.system.order.service.domain.usecase.trackorder;

import com.food.ordering.system.application.ports.input.query.Query;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class TrackOrderQuery extends Query {
    @NotNull
    private UUID orderTrackingId;
}
