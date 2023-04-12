package com.food.ordering.system.application.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDto.builder()
                .message("Unexpected error")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
    }

    @ExceptionHandler(value = {ValidationException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleException(ValidationException validationException) {
        ErrorDto errorDto;
        String message;

        if(validationException instanceof ConstraintViolationException) {
            message = extractViolationsFromException((ConstraintViolationException) validationException);
        } else {
            message = validationException.getMessage();
        }

        errorDto = ErrorDto.builder().code(HttpStatus.BAD_REQUEST.getReasonPhrase()).message(message).build();
        log.error(message, validationException);

        return errorDto;
    }

    private String extractViolationsFromException(ConstraintViolationException validationException) {
        return validationException.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("--"));
    }
}
