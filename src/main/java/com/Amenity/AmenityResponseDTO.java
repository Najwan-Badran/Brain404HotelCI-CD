package com.Amenity;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Amenity response object")
public record AmenityResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "Free WiFi")
        String name,
        @Schema(example = "High-speed wireless internet")
        String description,
        @Schema(example = "true")
        Boolean isActive
) {}