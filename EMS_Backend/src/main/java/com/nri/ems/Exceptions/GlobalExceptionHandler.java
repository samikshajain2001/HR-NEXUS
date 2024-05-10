package com.nri.ems.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.mail.MessagingException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        ApiError apiError = new ApiError(message, HttpStatus.NOT_FOUND);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ApiError> MessagingExceptionHandler(MessagingException ex) {
        String message = ex.getMessage();
        ApiError apiError = new ApiError(message, HttpStatus.NOT_FOUND);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyInputException.class)
    public ResponseEntity<ApiError> EmptyInputExceptionHandler(EmptyInputException ex) {
        String message = ex.getMessage();
        ApiError apiError = new ApiError(message, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<ApiError> UnAuthorizedAccessExceptionHandler(UnAuthorizedAccessException ex) {
        String message = ex.getMessage();
        ApiError apiError = new ApiError(message, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.UNAUTHORIZED);
    }

}
