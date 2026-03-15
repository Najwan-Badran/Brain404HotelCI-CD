package com.Amenity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface AmenityService {
    /* =========================CREATE========================= */
    AmenityResponseDTO create(AmenityRequestDTO requestDTO);
    /* =========================READ========================= */
    AmenityResponseDTO getById(Long id);
    Page<AmenityResponseDTO> getAllActive(Pageable pageable);
    Page<AmenityResponseDTO> getAll(Pageable pageable); // ADMIN
    Page<AmenityResponseDTO> getAllInactive(Pageable pageable); // ADMIN
    /* =========================UPDATE========================= */
    AmenityResponseDTO update(Long id, AmenityRequestDTO requestDTO);
    /* =========================DELETE / STATE========================= */
    void softDelete(Long id);
    void hardDelete(Long id);
    void reactivate(Long id);
}