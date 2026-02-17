package com.ExceptionHandler;

import com.ApiError;
import com.Exception.RoomTypeNotFoundException;
import com.Exception.RoomTypeAlreadyExistsException;
import com.Exception.RoomTypeBadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class RoomTypeExceptionHandler {

    @ExceptionHandler(RoomTypeNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(RoomTypeNotFoundException ex,
                                                   HttpServletRequest request) {

        ApiError body = new ApiError(
                Instant.now().toString(),
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(RoomTypeAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(RoomTypeAlreadyExistsException ex,
                                                        HttpServletRequest request) {

        ApiError body = new ApiError(
                Instant.now().toString(),
                409,
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(RoomTypeBadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(RoomTypeBadRequestException ex,
                                                     HttpServletRequest request) {

        ApiError body = new ApiError(
                Instant.now().toString(),
                400,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
