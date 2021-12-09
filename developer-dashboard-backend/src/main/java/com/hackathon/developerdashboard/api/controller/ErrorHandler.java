/*
 * Jumio Inc.
 *
 * Copyright (C) 2010 - 2021
 * All rights reserved.
 */
package com.hackathon.developerdashboard.api.controller;

import com.google.gson.Gson;
import com.hackathon.developerdashboard.api.dto.ErrorContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Gson gson;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorContainer> handle(ResponseStatusException ex) {
        log.error("ErrorHandler caught exception", ex);
        return ResponseEntity.status(ex.getStatus()).body(new ErrorContainer(ex.getReason()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorContainer handleIllegalArgument(IllegalArgumentException ex) {
        log.error("ErrorHandler caught exception", ex);
        return new ErrorContainer(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorContainer handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("ErrorHandler caught exception", ex);
        return new ErrorContainer(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorContainer handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("ErrorHandler caught exception", ex);
        return new ErrorContainer(buildValidationError(ex));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorContainer handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("ErrorHandler caught exception", ex);
        return new ErrorContainer(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorContainer handleGenericError(Exception ex) {
        log.error("ErrorHandler caught GENERIC exception", ex);
        return new ErrorContainer(ex.getMessage());
    }

    //~ Helpers

    private String buildValidationError(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .map(this::buildErrorLine)
                .collect(Collectors.joining("; "));
    }

    private String buildErrorLine(ObjectError error) {
        String fieldName = error.getObjectName();
        if (error instanceof FieldError) {
            fieldName = ((FieldError) error).getField();
        }
        return String.format("%s: %s", fieldName, error.getDefaultMessage());
    }

}

