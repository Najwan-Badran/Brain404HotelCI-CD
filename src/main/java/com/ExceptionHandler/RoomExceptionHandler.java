package com.ExceptionHandler;

import com.ApiError;
import com.Exception.RoomNotFoundException;
import com.Exception.RoomAlreadyExistsException;
import com.Exception.RoomBadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class RoomExceptionHandler {

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(RoomNotFoundException ex,
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

    @ExceptionHandler(RoomAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(RoomAlreadyExistsException ex,
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

    @ExceptionHandler(RoomBadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(RoomBadRequestException ex,
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
