package com.Amenity;
import org.springframework.stereotype.Component;
@Component
public class AmenityMapper {
    /* =========================
       CREATE
       ========================= */
    public Amenity toEntity(AmenityRequestDTO dto) {
        Amenity amenity = new Amenity();
        amenity.setName(safeTrim(dto.getName()));
        amenity.setDescription(safeTrim(dto.getDescription()));
        // Always default to true on create
        amenity.setIsActive(true);
        return amenity;
    }
    /* =========================
       UPDATE (Partial Update Safe)
       ========================= */
    public void updateEntity(Amenity amenity, AmenityRequestDTO dto) {
        if (dto.getName() != null) {
            amenity.setName(safeTrim(dto.getName()));
        }
        if (dto.getDescription() != null) {
            amenity.setDescription(safeTrim(dto.getDescription()));
        }
        // Only update isActive if explicitly sent
        if (dto.getIsActive() != null) {
            amenity.setIsActive(dto.getIsActive());
        }
    }
    /* =========================
       ENTITY → RESPONSE DTO
       ========================= */
    public AmenityResponseDTO toResponseDTO(Amenity amenity) {
        return new AmenityResponseDTO(
                amenity.getId(),
                amenity.getName(),
                amenity.getDescription(),
                amenity.getIsActive()
        );
    }
    /* =========================
       Helper
       ========================= */
    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}