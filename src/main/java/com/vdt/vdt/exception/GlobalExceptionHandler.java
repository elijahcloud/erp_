package com.vdt.vdt.exception;



import com.lowagie.text.DocumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ApiError> handleTicketNotFoundException(TicketNotFoundException ex) {
        ApiError error = new ApiError(
                "Ticket not found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                ZonedDateTime.now().toInstant()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ApiError> handleInvalidStatusException(InvalidStatusException ex) {
        ApiError error = new ApiError(
                "Invalid ticket status",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                ZonedDateTime.now().toInstant()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIOException(IOException ex) {
        return buildResponse("File I/O error occurred", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DocumentException.class)
    public ResponseEntity<ApiError> handleDocumentException(DocumentException ex) {
        return buildResponse("Error generating PDF document", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        return buildResponse("Unexpected error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiError> buildResponse(String error, String message, HttpStatus status) {
        ApiError apiError = new ApiError(error, message, status.value(), Instant.now());
        return new ResponseEntity<>(apiError, status);
    }
}
