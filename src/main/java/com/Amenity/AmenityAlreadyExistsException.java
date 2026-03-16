package com.Amenity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.CONFLICT)
public class AmenityAlreadyExistsException extends RuntimeException {
    public AmenityAlreadyExistsException(String name) {
        super("Amenity already exists with name: " + name);
    }
}