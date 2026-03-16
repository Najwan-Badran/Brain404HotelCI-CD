package com.Amenity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AmenityValidationException extends RuntimeException {
    public AmenityValidationException(String message) {
        super(message);
    }
}