package com.customer.controller.advice;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.customer.exceptions.ErrorData;
import com.customer.exceptions.ResourceNotFoundException;
import com.customer.exceptions.ValidationFailureException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author davidjmartin
 */
@ControllerAdvice
@Slf4j
public class CustomerControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorData handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {
        // collect all validation errors
        List<String> errors = ex.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());

        return ErrorData.builder()
            .errorCode("request validation failure.")
            .message(errors.toString())
            .url(request.getMethod() + " request to : " + request.getRequestURI())
            .timestamp(LocalDateTime.now().toString())
            .build();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorData handleDataAccessException(HttpServletRequest request, EmptyResultDataAccessException ex) {
        log.info("handling EmptyResultDataAccessException: {}.", ex.getMessage());
        return ErrorData.builder()
            .errorCode("INTERNAL_ERROR")
            .message(ex.getMessage())
            .url(request.getRequestURI())
            .timestamp(LocalDateTime.now().toString())
            .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorData handleEntityNotFound(HttpServletRequest request, ResourceNotFoundException ex) {
        log.info("handling EntityNotFoundException: {}.", ex.getMessage());
        return ErrorData.builder()
            .errorCode("NOT_FOUND")
            .message(ex.getMessage())
            .url(request.getRequestURI())
            .timestamp(LocalDateTime.now().toString())
            .build();
    }

    @ExceptionHandler(ValidationFailureException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorData handleValidationFailureException(HttpServletRequest request, ValidationFailureException ex) {
        log.info("handling ValidationFailureException with message: {}.", ex.getMessage());
        return ErrorData.builder()
            .errorCode("request validation failure.")
            .message(ex.getMessage())
            .url(request.getMethod() + " request to : " + request.getRequestURI())
            .timestamp(LocalDateTime.now().toString())
            .build();
    }

}