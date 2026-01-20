package com.hackerrank.sample.exception;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NoSuchResourceFoundException ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                Instant.now().toEpochMilli()
        );
    }

    @ExceptionHandler(BadResourceRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadResourceRequestException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                Instant.now().toEpochMilli()
        );
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce("", (a, b) -> a + "; " + b);

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed: " + errorMsg,
                Instant.now().toEpochMilli()
        );
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {
        String requiredType = "numeric";
        if (ex.getRequiredType() != null) {
            requiredType = switch (ex.getRequiredType().getSimpleName()) {
                case "Long", "Integer", "Double" -> "numeric";
                case "Boolean" -> "true/false";
                case "LocalDate", "OffsetDateTime" -> "date format";
                default -> ex.getRequiredType().getSimpleName().toLowerCase();
            };
        }
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                String.format("Invalid parameter: '%s' must be a %s", ex.getName(), requiredType),
                Instant.now().toEpochMilli());
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidJson(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        String message = "Malformed JSON request or invalid field values";

        if (ex.getMessage() != null && ex.getMessage().contains("Condition")) {
            message = "Invalid value for 'condition'. Allowed values are: [NEW, USED]";
        }

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                Instant.now().toEpochMilli()
        );
    }

    @ExceptionHandler({PropertyReferenceException.class, InvalidDataAccessApiUsageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSortErrors(Exception ex) {
        String message = "Error en los parámetros de la consulta";
        if (ex instanceof PropertyReferenceException) {
            message = "El campo de ordenamiento '" + ((PropertyReferenceException) ex).getPropertyName() + "' no existe";
        } else if (ex.getCause() instanceof PropertyReferenceException) {
            message = "El campo de ordenamiento '" + ((PropertyReferenceException) ex.getCause()).getPropertyName() + "' no existe";
        }

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                Instant.now().toEpochMilli()
        );
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(jakarta.validation.ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .reduce("", (a, b) -> a + "; " + b);

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Database constraint violation: " + message,
                Instant.now().toEpochMilli()
        );
    }
}