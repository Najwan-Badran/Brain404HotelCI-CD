package com.Image;

import java.util.List;

/**
 * Service interface for Image operations.
 */
public interface ImageService {

    ImageResponseDTO create(ImageRequestDTO dto);

    ImageResponseDTO getById(Long id);

    ImageResponseDTO update(Long id, ImageRequestDTO dto);

    void delete(Long id);

    List<ImageResponseDTO> getByEntity(ImageEntityType entityType, Long entityId);

    ImageResponseDTO setPrimary(Long imageId);
}
