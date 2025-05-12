package com.as.server.exception;

import com.as.server.dto.error.ApiError;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException e) {
        log.debug("EntityNotFoundException: {}", e.getMessage());
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.DATA_NOT_FOUND)
                .message(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException e) {
        log.debug("IllegalArgumentException: {}", e.getMessage());
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.DATA_NOT_FOUND)
                .message(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(org.springframework.security.access.AccessDeniedException e) {
        log.debug("AccessDeniedException: {}", e.getMessage());
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.PERMISSION_DENIED)
                .message(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException e) {
        log.debug("ConflictException: {}", e.getMessage());
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.CONFLICT)
                .message(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException e) {
        log.debug("IllegalStateException: {}", e.getMessage());
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.CONFLICT)
                .message(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException e) {
        log.debug("MethodArgumentNotValidException: {}", e.getMessage());
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        String message = String.format("Validation failed for request: %s %s", fieldError.getField(), fieldError.getDefaultMessage());
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.DATA_NOT_FOUND)
                .message(message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ApiError> handleInvalidFormatException(InvalidFormatException e) {
        log.debug("InvalidFormatException: value={}, cause={}", e.getValue(), e.getCause());
        String message = e.getCause() instanceof IllegalArgumentException ?
                e.getCause().getMessage() : "Invalid value for parameter: " + e.getValue();
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.DATA_NOT_FOUND)
                .message(message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.debug("MethodArgumentTypeMismatchException: param={}, value={}", e.getName(), e.getValue());
        String message = e.getCause() instanceof IllegalArgumentException ?
                e.getCause().getMessage() : "Invalid value for parameter " + e.getName() + ": " + e.getValue();
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.DATA_NOT_FOUND)
                .message(message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNullPointerException(NullPointerException e) {
        log.debug("NullPointerException: {}", e.getMessage());
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.DATA_NOT_FOUND)
                .message("Unexpected null value: " + (e.getMessage() != null ? e.getMessage() : "Unknown"));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}