package com.Image;

import org.springframework.stereotype.Component;

/**
 * Mapper for Image entity and DTOs.
 */
@Component
public class ImageMapper {

    public Image toEntity(ImageRequestDTO dto) {
        Image image = new Image();
        image.setUrl(dto.getUrl());
        image.setAltText(dto.getAltText());
        image.setEntityType(dto.getEntityType());
        image.setEntityId(dto.getEntityId());
        image.setPrimary(dto.isPrimary());
        image.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        return image;
    }

    public void updateEntity(Image image, ImageRequestDTO dto) {
        if (dto.getUrl() != null) {
            image.setUrl(dto.getUrl());
        }
        if (dto.getAltText() != null) {
            image.setAltText(dto.getAltText());
        }
        if (dto.getSortOrder() != null) {
            image.setSortOrder(dto.getSortOrder());
        }
        image.setPrimary(dto.isPrimary());
    }

    public ImageResponseDTO toResponseDTO(Image image) {
        ImageResponseDTO dto = new ImageResponseDTO();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setAltText(image.getAltText());
        dto.setEntityType(image.getEntityType());
        dto.setEntityId(image.getEntityId());
        dto.setPrimary(image.isPrimary());
        dto.setSortOrder(image.getSortOrder());
        dto.setFileName(image.getFileName());
        dto.setContentType(image.getContentType());
        dto.setFileSize(image.getFileSize());
        dto.setCreatedAt(image.getCreatedAt());
        return dto;
    }
}
