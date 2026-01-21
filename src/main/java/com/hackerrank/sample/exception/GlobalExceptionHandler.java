package com.hackerrank.sample.exception;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles resources not found in the system.
     */
    @ExceptionHandler(NoSuchResourceFoundException.class)
    public ProblemDetail handleNotFound(final NoSuchResourceFoundException ex) {
        return createProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "Resource Not Found");
    }

    /**
     * Handles security exceptions related to invalid or missing tokens.
     * This ensures we return 401 instead of a generic 403.
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidToken(final InvalidTokenException ex) {
        return createProblemDetail(HttpStatus.UNAUTHORIZED, ex.getMessage(), "Security Error");
    }

    /**
     * Handles generic bad requests for resources.
     */
    @ExceptionHandler(BadResourceRequestException.class)
    public ProblemDetail handleBadRequest(final BadResourceRequestException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), "Bad Request");
    }

    /**
     * Handles @Valid annotation failures.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(final MethodArgumentNotValidException ex) {
        final var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return createProblemDetail(HttpStatus.BAD_REQUEST, "Validation failed: " + errors, "Validation Error");
    }

    /**
     * Handles parameter type mismatches in URLs.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        var requiredType = "numeric"; // Standard 1: var used

        if (ex.getRequiredType() != null) {
            requiredType = switch (ex.getRequiredType().getSimpleName()) {
                case "Long", "Integer", "Double" -> "numeric";
                case "Boolean" -> "true/false";
                case "LocalDate", "OffsetDateTime" -> "date format";
                default -> ex.getRequiredType().getSimpleName().toLowerCase();
            };
        }

        final var detail = String.format("Invalid parameter: '%s' must be a %s", ex.getName(), requiredType);
        return createProblemDetail(HttpStatus.BAD_REQUEST, detail, "Type Mismatch");
    }

    /**
     * Handles malformed JSON or invalid enum values.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidJson(final HttpMessageNotReadableException ex) {
        var message = "Malformed JSON request or invalid field values";

        // Guard Clause for specific condition error [Standard 10]
        if (ex.getMessage() != null && ex.getMessage().contains("Condition")) {
            message = "Invalid value for 'condition'. Allowed values are: [NEW, USED]";
        }

        return createProblemDetail(HttpStatus.BAD_REQUEST, message, "Invalid JSON");
    }

    /**
     * Handles errors related to database sorting fields.
     */
    @ExceptionHandler({PropertyReferenceException.class, InvalidDataAccessApiUsageException.class})
    public ProblemDetail handleSortErrors(final Exception ex) {
        var message = "Invalid query parameters";

        if (ex instanceof PropertyReferenceException pre) {
            message = "Sorting field '" + pre.getPropertyName() + "' does not exist";
        } else if (ex.getCause() instanceof PropertyReferenceException preCause) {
            message = "Sorting field '" + preCause.getPropertyName() + "' does not exist";
        }

        return createProblemDetail(HttpStatus.BAD_REQUEST, message, "Sorting Error");
    }

    /**
     * Helper method to standardize ProblemDetail creation.
     */
    private ProblemDetail createProblemDetail(final HttpStatus status, final String detail, final String title) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("timestamp", Instant.now()); // Standard 9: Instant used
        return problemDetail;
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(org.springframework.security.core.AuthenticationException ex) {
        var problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Token is missing or invalid"
        );
        problemDetail.setTitle("Unauthorized Access");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}