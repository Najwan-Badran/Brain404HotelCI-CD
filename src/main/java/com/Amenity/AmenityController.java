package com.Amenity;

import com.PagedResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/amenities")
@Tag(name = "Amenity", description = "Amenity Management APIs")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    /* =========================
       CREATE (ADMIN)
       ========================= */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new amenity (ADMIN)")
    public ResponseEntity<AmenityResponseDTO> create(
            @Valid @RequestBody AmenityRequestDTO requestDTO) {

        AmenityResponseDTO created = amenityService.create(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /* =========================
       GET BY ID
       ========================= */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get amenity by ID")
    public ResponseEntity<AmenityResponseDTO> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                amenityService.getById(id)
        );
    }

    /* =========================
       GET ACTIVE
       ========================= */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get all active amenities")
    public ResponseEntity<PagedResponse<AmenityResponseDTO>> getAllActive(
            Pageable pageable) {

        Page<AmenityResponseDTO> page =
                amenityService.getAllActive(pageable);

        return ResponseEntity.ok(
                PagedResponse.from(page)
        );
    }

    /* =========================
       ADMIN ENDPOINTS
       ========================= */

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all amenities (ADMIN)")
    public ResponseEntity<PagedResponse<AmenityResponseDTO>> getAll(
            Pageable pageable) {

        return ResponseEntity.ok(
                PagedResponse.from(
                        amenityService.getAll(pageable)
                )
        );
    }

    @GetMapping("/admin/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all inactive amenities (ADMIN)")
    public ResponseEntity<PagedResponse<AmenityResponseDTO>> getAllInactive(
            Pageable pageable) {

        return ResponseEntity.ok(
                PagedResponse.from(
                        amenityService.getAllInactive(pageable)
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update amenity (ADMIN)")
    public ResponseEntity<AmenityResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AmenityRequestDTO requestDTO) {

        return ResponseEntity.ok(
                amenityService.update(id, requestDTO)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete amenity (ADMIN)")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {

        amenityService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Hard delete amenity permanently (ADMIN)")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {

        amenityService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reactivate amenity (ADMIN)")
    public ResponseEntity<Void> reactivate(@PathVariable Long id) {

        amenityService.reactivate(id);
        return ResponseEntity.noContent().build();
    }
}