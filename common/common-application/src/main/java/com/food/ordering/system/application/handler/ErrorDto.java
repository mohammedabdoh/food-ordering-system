package com.food.ordering.system.application.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public record ErrorDto(String message, String code) {
}
