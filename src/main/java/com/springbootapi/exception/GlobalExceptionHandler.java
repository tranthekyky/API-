package com.springbootapi.exception;


import com.springbootapi.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

import java.util.List;



@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleDateNotFoundException(EntityNotFoundException ex , WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errorMessage(HttpStatus.BAD_REQUEST.name())
                .message(List.of(ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoResourceFoundException.class )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleUrlNotFoundException(NoResourceFoundException ex , WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .errorMessage(HttpStatus.NOT_FOUND.name())
                .message(List.of("URL not found"))
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({HttpMessageNotReadableException.class , DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleRequestBodyNotDataException( WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errorMessage(HttpStatus.BAD_REQUEST.name())
                .message(List.of("Required request body is missing"))
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex , WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map((error) -> error.getField() + " : " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
          ErrorResponse.builder()
                  .status(HttpStatus.BAD_REQUEST.value())
                  .errorMessage(HttpStatus.BAD_REQUEST.name())
                  .message(errors)
                  .timestamp(LocalDateTime.now())
                  .path(request.getDescription(false))
                  .build()
        );

    }

}
