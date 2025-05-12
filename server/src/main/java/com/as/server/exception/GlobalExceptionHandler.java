package com.as.server.exception;

import com.as.server.dto.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException e) {
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.DATA_NOT_FOUND)
                .message(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException e) {
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.DATA_NOT_FOUND)
                .message(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(org.springframework.security.access.AccessDeniedException e) {
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.PERMISSION_DENIED)
                .message("Access is denied");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException e) {
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.CONFLICT)
                .message(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException e) {
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.CONFLICT)
                .message(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        String message = String.format("Validation failed for request: %s %s", fieldError.getField(), fieldError.getDefaultMessage());
        ApiError error = new ApiError()
                .code(ApiError.CodeEnum.DATA_NOT_FOUND)
                .message(message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}