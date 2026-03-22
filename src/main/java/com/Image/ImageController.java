package com.Image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Image management.
 */
@RestController
@RequestMapping("/images")
@Tag(name = "Images", description = "Image management endpoints")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Upload image", description = "Create a new image record")
    public ResponseEntity<ImageResponseDTO> create(@Valid @RequestBody ImageRequestDTO dto) {
        ImageResponseDTO created = imageService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get image by ID", description = "Retrieve an image by its ID")
    public ImageResponseDTO getById(@PathVariable Long id) {
        return imageService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get images by entity", description = "Retrieve all images for a specific entity")
    public List<ImageResponseDTO> getByEntity(
            @RequestParam ImageEntityType entityType,
            @RequestParam Long entityId) {
        return imageService.getByEntity(entityType, entityId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update image", description = "Update an existing image")
    public ImageResponseDTO update(@PathVariable Long id, @Valid @RequestBody ImageRequestDTO dto) {
        return imageService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete image", description = "Delete an image")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/primary")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Set primary image", description = "Set an image as the primary image for its entity")
    public ImageResponseDTO setPrimary(@PathVariable Long id) {
        return imageService.setPrimary(id);
    }
}
