package com.foro.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.foro.api.model.dto.error.ApiError;
import com.foro.api.model.dto.error.ValidationError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandling {

    @ExceptionHandler({
            ResourceNotFoundException.class,
            EntityNotFoundException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleNotFoundException(
            Exception e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, "unable to find requested resource");
    }

    @ExceptionHandler({DuplicateResourceException.class, InvalidDataAccessApiUsageException.class})
    public ResponseEntity<ApiError> handleInvalidDataException(
            Exception e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler({InsufficientAuthenticationException.class, TopicClosedException.class})
    public ResponseEntity<ApiError> handleForbiddenException(
            InsufficientAuthenticationException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(
            BadCredentialsException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        List<ValidationError> errors = e.getFieldErrors().stream()
                    .map(fieldError -> new ValidationError(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Validation failed for one or more fields",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors);

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request) {

        List<ValidationError> errors = new ArrayList<>();

        if (e.getRootCause() instanceof InvalidFormatException exception){

            errors.add(new ValidationError(exception.getPath().get(0).getFieldName(),
                    "Invalid value: '" + exception.getValue() + "'"));


        }else {
            errors.add(new ValidationError("requestBody", "Invalid request body"));
        }


        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Validation failed for one or more fields",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errors);

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(
            DataIntegrityViolationException e, HttpServletRequest request){
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<ApiError> buildErrorResponse(
            HttpServletRequest request, HttpStatus status, String message) {
        ApiError apiError = new ApiError(
                    request.getRequestURI(),
                    message,
                    status.value(),
                    LocalDateTime.now(),
                    null
            );
        return new ResponseEntity<>(apiError, status);
    }
}
