package com.Amenity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AmenityNotFoundException extends RuntimeException {
    public AmenityNotFoundException(Long id) {
        super("Amenity not found with id: " + id);
    }
}