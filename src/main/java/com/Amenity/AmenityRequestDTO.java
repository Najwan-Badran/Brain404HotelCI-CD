package com.Amenity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AmenityRequestDTO {
    @NotBlank(message = "Amenity name cannot be empty")
    @Size(max = 80, message = "Amenity name must be at most 80 characters")
    @Schema(example = "Free WiFi")
    private String name;
    @Size(max = 500, message = "Description must be at most 500 characters")
    @Schema(example = "High-speed wireless internet available in all rooms")
    private String description;
    @Schema(example = "true")
    private Boolean isActive;
}