package com.Image;

/**
 * Exception thrown when an image is not found.
 */
public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(Long id) {
        super("Image not found with id: " + id);
    }

    public ImageNotFoundException(String message) {
        super(message);
    }
}
