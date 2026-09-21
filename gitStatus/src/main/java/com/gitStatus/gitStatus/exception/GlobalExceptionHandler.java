// src/main/java/com/gitStatus/gitStatus/exception/GlobalExceptionHandler.java
package com.gitStatus.gitStatus.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

/**
 * Centralized error handling. Returns RFC 7807 ProblemDetail (ruleset §15).
 * Never leaks stack traces, SQL, or internal class names.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Resource not found");
        pd.setType(URI.create("urn:problem:resource-not-found"));
        pd.setProperty("errorCode", "RESOURCE_NOT_FOUND");
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraint(ConstraintViolationException ex) {
        String detail = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .findFirst()
                .orElse("Validation failed");
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, detail);
        pd.setTitle("Validation failed");
        pd.setType(URI.create("urn:problem:validation-failed"));
        pd.setProperty("errorCode", "VALIDATION_FAILED");
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArg(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, detail);
        pd.setTitle("Validation failed");
        pd.setType(URI.create("urn:problem:validation-failed"));
        pd.setProperty("errorCode", "VALIDATION_FAILED");
        return pd;
    }
}