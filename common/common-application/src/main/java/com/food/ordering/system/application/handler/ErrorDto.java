package com.food.ordering.system.application.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ErrorDto {
    private String message;
    private String code;
}
