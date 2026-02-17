package com.Exception;

public class RoomTypeNotFoundException extends RuntimeException {
    public RoomTypeNotFoundException(Long id) {
        super("RoomType not found with id: " + id);

    }
}
